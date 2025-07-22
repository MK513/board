package kkmm.back.board.web.controller;

import kkmm.back.board.ControllerTestSupport;
import kkmm.back.board.domain.dto.NoteDto;
import kkmm.back.board.domain.model.Category;
import kkmm.back.board.domain.model.Member;
import kkmm.back.board.web.SessionConst;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class NoteControllerTest extends ControllerTestSupport {

    @Test
    void 글쓰기_폼() throws Exception {
        Member member = joinMember("note@test.com", "1", "name");
        createCategory("cat");

        mockMvc.perform(get("/note/write")
                        .sessionAttr(SessionConst.LOGIN_MEMBER, member))
                .andExpect(status().isOk())
                .andExpect(view().name("form/writeNoteForm"))
                .andExpect(model().attributeExists("categories", "note"));
    }

    @Test
    void 게시글_조회() throws Exception {
        Member member = joinMember("note2@test.com", "1", "name");
        Category category = createCategory("cat2");
        NoteDto dto = new NoteDto();
        dto.setTitle("t");
        dto.setContents("c");
        Long id = noteService.save(dto, member, category);

        mockMvc.perform(get("/note/view/{id}", id)
                        .sessionAttr(SessionConst.LOGIN_MEMBER, member))
                .andExpect(status().isOk())
                .andExpect(view().name("form/viewNoteForm"))
                .andExpect(model().attributeExists("note", "comments", "updateComment", "newComment"));
    }
}
