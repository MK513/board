package kkmm.back.board.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import kkmm.back.board.domain.Service.CategoryService;
import kkmm.back.board.domain.Service.CommentService;
import kkmm.back.board.domain.Service.NoteService;
import kkmm.back.board.domain.model.*;
import kkmm.back.board.web.argumentResolver.Login;
import kkmm.back.board.web.dto.CommentForm;
import kkmm.back.board.web.dto.NoteForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/note")
public class NoteController {

    private final CategoryService categoryService;
    private final NoteService noteService;
    private final CommentService commentService;

    @Value("${file.dir}")
    private String fileDir;

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
                           Model model) throws IOException {

        log.info("noteForm={}", noteForm);
        log.info("file={}", noteForm.getFiles());
        log.info("id={}", noteForm.getCategoryId());

        Category category = categoryService.findById(noteForm.getCategoryId());

        Long id = noteService.saveNote(noteForm, member, category);

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

        model.addAttribute("note", noteForm);
        model.addAttribute("comments", commentForms);
        model.addAttribute("updateComment", new CommentForm());
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
                           Model model) {

        log.info("noteForm.getDeleteFiles()={}", noteForm.getDeleteFiles());
        log.info("noteForm.getDeleteImages()={}", noteForm.getDeleteImages());

        noteService.updateNote(id, noteForm);

        return "redirect:/note/view/" + id;
    }

    @DeleteMapping("/delete/{id}")
    public String deleteNote(@PathVariable Long id) {

        noteService.deleteNote(id);

        return "redirect:/board/list";
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileDir + filename);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam("sn") String storeFileName,
                                                @RequestParam("un") String uploadFileName,
                                                Model model) throws MalformedURLException {

        UrlResource resource = new UrlResource("file:" + fileDir + storeFileName);
        String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }

}
