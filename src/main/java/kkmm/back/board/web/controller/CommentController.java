package kkmm.back.board.web.controller;

import kkmm.back.board.domain.Service.CommentService;
import kkmm.back.board.domain.Service.NoteService;
import kkmm.back.board.domain.model.Comment;
import kkmm.back.board.domain.model.Member;
import kkmm.back.board.domain.model.Note;
import kkmm.back.board.web.argumentResolver.Login;
import kkmm.back.board.web.dto.CommentForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final NoteService noteService;

    @PostMapping("/create/{id}")
    public String createComment(@Validated @ModelAttribute("comment") CommentForm commentForm,
                                @PathVariable("id") Long noteId,
                                @Login Member member) {

        log.info("createComment={}", commentForm);

        Note note = noteService.findOne(noteId);
        Comment parentComment = commentForm.getParentId() == null ? null : commentService.findOne(commentForm.getParentId());

        Comment comment = new Comment(commentForm, member, note, parentComment);
        commentService.save(comment);

        return "redirect:/note/view/" + noteId;
    }

    @PutMapping("/edit/{id}")
    public String editComment(@ModelAttribute("updateComment") CommentForm commentForm,
                              @PathVariable("id") Long noteId
                              ) {

        commentService.updateComment(commentForm.getId(), commentForm.getContents());

        return "redirect:/note/view/" + noteId;
    }

    @DeleteMapping("/delete/{id}")
    public String deleteComment(@RequestParam("commentId") Long commentId,
                                @PathVariable("id") Long noteId) {

        commentService.deleteComment(commentId);

        return "redirect:/note/view/" + noteId;
    }
}
