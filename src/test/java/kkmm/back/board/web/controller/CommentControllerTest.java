package kkmm.back.board.web.controller;

import kkmm.back.board.ControllerTestSupport;
import kkmm.back.board.domain.dto.CommentDto;
import kkmm.back.board.domain.dto.NoteDto;
import kkmm.back.board.domain.model.Category;
import kkmm.back.board.domain.model.Member;
import kkmm.back.board.web.SessionConst;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommentControllerTest extends ControllerTestSupport {

    @Test
    void 댓글_작성() throws Exception {
        Member member = joinMember("comment@test.com", "1", "name");
        Category category = createCategory("cat");
        NoteDto dto = new NoteDto();
        dto.setTitle("t");
        dto.setContents("c");
        Long noteId = noteService.save(dto, member, category);

        mockMvc.perform(post("/comment/create/{id}", noteId)
                        .sessionAttr(SessionConst.LOGIN_MEMBER, member)
                        .param("contents", "hello"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/note/view/" + noteId));
    }

    @Test
    void 댓글_삭제() throws Exception {
        Member member = joinMember("comment@test.com", "1", "name");
        Category category = createCategory("cat");
        NoteDto dto = new NoteDto();
        dto.setTitle("t");
        dto.setContents("c");
        Long noteId = noteService.save(dto, member, category);
        Long commentId = saveNewComment("content", noteId, member);

        mockMvc.perform(delete("/comment/delete/{id}", noteId)
                        .sessionAttr(SessionConst.LOGIN_MEMBER, member)
                        .param("commentId", commentId.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/note/view/" + noteId));

        assertThatThrownBy(() -> commentService.findById(commentId))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void 댓글_수정() throws Exception {
        Member member = joinMember("comment@test.com", "1", "name");
        Category category = createCategory("cat");
        NoteDto dto = new NoteDto();
        dto.setTitle("t");
        dto.setContents("c");
        Long noteId = noteService.save(dto, member, category);
        Long commentId = saveNewComment("content", noteId, member);

        mockMvc.perform(put("/comment/edit/{id}", noteId)
                        .sessionAttr(SessionConst.LOGIN_MEMBER, member)
                        .param("id", commentId.toString())
                        .param("contents", "Edit!"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/note/view/" + noteId));

        assertThat(commentService.findById(commentId).getContents()).isEqualTo("Edit!");
    }

    private Long saveNewComment(String content, Long noteId, Member member) {
        CommentDto commentDto = new CommentDto();
        commentDto.setNoteId(noteId);
        commentDto.setContents(content);
        return commentService.save(commentDto, member);
    }
}
