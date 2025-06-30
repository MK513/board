package kkmm.back.board.web.controller;

import jakarta.servlet.http.HttpSession;
import kkmm.back.board.domain.Service.CategoryService;
import kkmm.back.board.domain.Service.CommentService;
import kkmm.back.board.domain.Service.NoteService;
import kkmm.back.board.domain.model.Category;
import kkmm.back.board.domain.model.Comment;
import kkmm.back.board.domain.model.Member;
import kkmm.back.board.domain.model.Note;
import kkmm.back.board.web.SessionConst;
import kkmm.back.board.web.model.CommentForm;
import kkmm.back.board.web.model.NoteForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
@Slf4j
public class NoteController {

    private final NoteService noteService;
    private final CommentService commentService;
    private final CategoryService categoryService;

//    TODO 게시판 말머리
//    TODO 게시판 분리
//    TODO 검색 기능 추가
//    TODO 파일 올리기 (사진, 동영상)

    @GetMapping("/list")
    public String listForm(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {

        List<NoteForm> noteForms = noteService.findPage(page).stream().map(NoteForm::new).collect(Collectors.toList());
        Long totalPages = (noteService.findNoteCount() / 10) + 1;
        String newCategory = "";

        log.info("totalPages: {}", totalPages);

        model.addAttribute("newCategory", newCategory);
        model.addAttribute("notes", noteForms);
        model.addAttribute("totalPages", totalPages);
        return "board/listForm";
    }

    @GetMapping("/write")
    public String createForm(Model model) {
        List<Category> categories = categoryService.findAll();

        model.addAttribute("categories", categories);
        model.addAttribute("note", new NoteForm());

        return "board/writeNoteForm";
    }

    @PostMapping("/write")
    public String saveNote(@Validated @ModelAttribute("note") NoteForm noteForm, Model model, HttpSession session) {

        log.info("noteForm={}", noteForm);
        log.info("id={}", noteForm.getCategoryId());

        Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        Category category = categoryService.findOne(noteForm.getCategoryId());
        Note note = new Note(noteForm, member, category);

        log.info("note={}",note);

        noteService.saveNote(note);
        categoryService.increaseCount(category);

        return "redirect:/board/list";
    }

    @GetMapping("/view/{id}")
    public String viewForm(@PathVariable Long id, Model model, HttpSession session) {

        Note note = noteService.findOne(id);
        noteService.increaseViewCount(id);

        List<Comment> comments = commentService.findComments(id);
        List<CommentForm> commentForms = comments.stream().map(CommentForm::new).collect(Collectors.toList());

        NoteForm noteForm = new NoteForm(note);

        // TODO 게시글, 댓글 출력시 개행 문자 적용 필요 - 각 줄별로 div 따로 적용?
        model.addAttribute("note", noteForm);
        model.addAttribute("comments", commentForms);
        model.addAttribute("newComment", new CommentForm(getUsername(session)));

        return "board/viewNoteForm";
    }

    private String getUsername(HttpSession session) {
        Member member = (session == null) ? null : (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        return (member == null) ? "익명" : member.getName();
    }

}
