package kkmm.back.board.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import kkmm.back.board.domain.Service.NoteService;
import kkmm.back.board.web.dto.NoteForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
@Slf4j
public class BoardController {

    private final NoteService noteService;

    @ModelAttribute("requestURI")
    public String requestURI(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @GetMapping("/list")
    public String listForm(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {

        List<NoteForm> noteForms = noteService.findPage(page).stream().map(NoteForm::new).collect(Collectors.toList());
        Long totalPages = noteService.countTotalPages();
        String categoryForm = "";

        log.info("totalPages: {}", totalPages);

        addListFormToModel(model, categoryForm, noteForms, totalPages);
        return "form/listForm";
    }

    @GetMapping("/search")
    public String searchResult(@RequestParam(value = "searchType", defaultValue = "") String searchType,
                               @RequestParam(value = "keyword", defaultValue = "") String keyword,
                               @RequestParam(value = "page", defaultValue = "1") int page,
                               Model model) {

        List<NoteForm> noteForms = noteService.searchPage(page, keyword, searchType).stream().map(NoteForm::new).collect(Collectors.toList());
        Long totalPages = noteService.countSearchedPages(keyword, searchType);
        String categoryForm = "";

        log.info("totalPages: {}", totalPages);

        addListFormToModel(model, categoryForm, noteForms, totalPages);
        return "form/listForm";
    }

    private static void addListFormToModel(Model model, String categoryForm, List<NoteForm> noteForms, Long totalPages) {
        model.addAttribute("categoryForm", categoryForm);
        model.addAttribute("notes", noteForms);
        model.addAttribute("totalPages", totalPages);
    }

}
