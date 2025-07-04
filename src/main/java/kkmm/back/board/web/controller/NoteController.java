package kkmm.back.board.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import kkmm.back.board.domain.Service.CategoryService;
import kkmm.back.board.domain.Service.CommentService;
import kkmm.back.board.domain.Service.MemberService;
import kkmm.back.board.domain.Service.NoteService;
import kkmm.back.board.domain.model.Category;
import kkmm.back.board.domain.model.Comment;
import kkmm.back.board.domain.model.Member;
import kkmm.back.board.domain.model.Note;
import kkmm.back.board.web.argumentResolver.Login;
import kkmm.back.board.web.model.CommentForm;
import kkmm.back.board.web.model.NoteForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/note")
public class NoteController {

    private final CategoryService categoryService;
    private final MemberService memberService;
    private final NoteService noteService;
    private final CommentService commentService;

//    TODO 파일 올리기 (사진, 동영상)

    @ModelAttribute("requestURI")
    public String requestURI(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @GetMapping("/write")
    public String createForm(Model model) {
        List<Category> categories = categoryService.findAll();

        model.addAttribute("categories", categories);
        model.addAttribute("note", new NoteForm());

        return "form/writeNoteForm";
    }

    @PostMapping("/write")
    public String saveNote(@Validated @ModelAttribute("note") NoteForm noteForm,
                           @Login Member member,
                           RedirectAttributes redirectAttributes,
                           Model model) {

        log.info("noteForm={}", noteForm);
        log.info("id={}", noteForm.getCategoryId());

        Category category = categoryService.findOne(noteForm.getCategoryId());
        Note note = new Note(noteForm, member, category);

        Long id = noteService.saveNote(note);
        categoryService.increaseCount(category);

        redirectAttributes.addAttribute("id", id);

        return "redirect:/note/view/{id}";
    }

    @GetMapping("/view/{id}")
    public String viewForm(@PathVariable Long id,
                           @Login Member member,
                           Model model) {

        Note note = noteService.findOne(id);
        noteService.increaseViewCount(id);

        List<Comment> comments = commentService.findComments(id);
        List<CommentForm> commentForms = comments.stream().map(CommentForm::new).collect(Collectors.toList());

        log.info("note={}", note);

        NoteForm noteForm = new NoteForm(note);

        // TODO 게시글, 댓글 출력시 개행 문자 적용 필요 - 각 줄별로 div 따로 적용?
        model.addAttribute("note", noteForm);
        model.addAttribute("comments", commentForms);
        model.addAttribute("newComment", new CommentForm(member != null ? member.getName() : null));

        return "form/viewNoteForm";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        List<Category> categories = categoryService.findAll();
        NoteForm noteForm = new NoteForm(noteService.findOne(id));

        model.addAttribute("categories", categories);
        model.addAttribute("note", noteForm);

        return "form/writeNoteForm";
    }

    @PutMapping("/edit/{id}")
    public String editNote(@Validated @ModelAttribute("note") NoteForm noteForm,
                           @PathVariable Long id,
                           Model model,
                           RedirectAttributes redirectAttributes) {

        Note note = noteService.findOne(id);
        note.updateContent(noteForm);
        noteService.saveNote(note);

        redirectAttributes.addAttribute("id", id);
        return "redirect:/note/view/{id}";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);

        return "redirect:/board/list";
    }

}
