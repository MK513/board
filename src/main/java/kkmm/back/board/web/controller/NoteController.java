package kkmm.back.board.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import kkmm.back.board.Service.MemberService;
import kkmm.back.board.Service.NoteService;
import kkmm.back.board.domain.model.Member;
import kkmm.back.board.domain.model.Note;
import kkmm.back.board.web.SessionConst;
import kkmm.back.board.web.model.NoteForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
@Slf4j
public class NoteController {

    private final NoteService noteService;
    private final MemberService memberService;

    @GetMapping("/list")
    public String listForm(Model model) {
        model.addAttribute("notes", noteService.findNotes());
        return "board/listForm";
    }

    @GetMapping("/write")
    public String createForm(Model model) {
        model.addAttribute("note", new NoteForm());
        return "board/writeNoteForm";
    }

    @PostMapping("/write")
    public String saveNote(@Validated @ModelAttribute("note") NoteForm noteForm, Model model,
                           HttpServletRequest request) {
        Member member = (Member) request.getSession(false)
                                        .getAttribute(SessionConst.LOGIN_MEMBER);

        Note note = new Note(noteForm, member);

        noteService.saveNote(note);

        return "redirect:/board/list";
    }

    @GetMapping("/view/{id}")
    public String viewForm(@PathVariable Long id, Model model) {

        Note note = noteService.findOne(id);

        model.addAttribute("note", note);

        return "board/viewNoteForm";
    }

}
