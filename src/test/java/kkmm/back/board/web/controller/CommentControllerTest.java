package kkmm.back.board.web.controller;

import kkmm.back.board.ControllerTestSupport;
import kkmm.back.board.domain.dto.NoteDto;
import kkmm.back.board.domain.model.Category;
import kkmm.back.board.domain.model.Member;
import kkmm.back.board.web.SessionConst;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        Long id = noteService.save(dto, member, category);

        mockMvc.perform(post("/comment/create/{id}", id)
                        .sessionAttr(SessionConst.LOGIN_MEMBER, member)
                        .param("contents", "hello"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/note/view/" + id));
    }
}
