package kkmm.back.board.web.controller;

import kkmm.back.board.domain.Service.CommentService;
import kkmm.back.board.domain.dto.CommentDto;
import kkmm.back.board.domain.model.Member;
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

    @PostMapping("/create/{id}")
    public String createComment(@Validated @ModelAttribute("comment") CommentForm commentForm,
                                @PathVariable("id") Long noteId,
                                @Login Member member) {

        log.info("createComment={}", commentForm);

        CommentDto commentDto = new CommentDto(commentForm);
        commentDto.setNoteId(noteId);
        commentService.save(commentDto, member);

        return "redirect:/note/view/" + noteId;
    }

    @PutMapping("/edit/{id}")
    public String editComment(@ModelAttribute("updateComment") CommentForm commentForm,
                              @PathVariable("id") Long noteId
                              ) {

        commentService.update(commentForm.getId(), commentForm.getContents());

        return "redirect:/note/view/" + noteId;
    }

    @DeleteMapping("/delete/{id}")
    public String deleteComment(@RequestParam("commentId") Long commentId,
                                @PathVariable("id") Long noteId) {

        commentService.deleteComment(commentId);

        return "redirect:/note/view/" + noteId;
    }
}
