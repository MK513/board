package kkmm.back.board.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kkmm.back.board.domain.Service.CommentService;
import kkmm.back.board.domain.Service.NoteService;
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

    @GetMapping("/list")
    public String listForm(Model model) {

        List<NoteForm> noteForms = noteService.findNotes().stream().map(NoteForm::new).collect(Collectors.toList());

        model.addAttribute("notes", noteForms);
        return "board/listForm";
    }

    @GetMapping("/write")
    public String createForm(Model model) {
        model.addAttribute("note", new NoteForm());
        return "board/writeNoteForm";
    }

    @PostMapping("/write")
    public String saveNote(@Validated @ModelAttribute("note") NoteForm noteForm, Model model, HttpSession session) {

        Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        Note note = new Note(noteForm, member);

        noteService.saveNote(note);

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
