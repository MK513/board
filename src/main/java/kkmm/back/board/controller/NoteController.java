package kkmm.back.board.controller;

import kkmm.back.board.Service.MemberService;
import kkmm.back.board.Service.NoteService;
import kkmm.back.board.domain.Member;
import kkmm.back.board.domain.Note;
import kkmm.back.board.domain.NoteDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
        model.addAttribute("noteDTO", new NoteDTO());
        return "board/writeNoteForm";
    }

    @PostMapping("/write")
    public String saveNote(NoteDTO noteDTO, Model model) {

        Member member = memberService.findByEmail("123");

        Note note = new Note(noteDTO, member);

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
