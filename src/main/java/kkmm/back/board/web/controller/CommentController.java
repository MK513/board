package kkmm.back.board.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import kkmm.back.board.domain.Service.CommentService;
import kkmm.back.board.domain.Service.NoteService;
import kkmm.back.board.domain.model.Comment;
import kkmm.back.board.domain.model.Member;
import kkmm.back.board.domain.model.Note;
import kkmm.back.board.web.SessionConst;
import kkmm.back.board.web.argumentResolver.Login;
import kkmm.back.board.web.model.CommentForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final NoteService noteService;

    // TODO 로그인 없이도 이용 가능하게 만들기? Comment와 Member를 느슨하게 연결할 방법 필요
    @PostMapping("/create/{id}")
    public String createComment(@Validated @ModelAttribute("comment") CommentForm commentForm,
                                @PathVariable("id") Long note_id,
                                @Login Member member) {

        log.info("createComment={}", commentForm);

        Comment parentComment = commentService.findOne(commentForm.getParentId());
        Note note = noteService.findOne(note_id);

        Comment comment = new Comment(commentForm, member, note, parentComment);

        commentService.save(comment);

        return "redirect:/note/view/" + note_id;
    }
}
