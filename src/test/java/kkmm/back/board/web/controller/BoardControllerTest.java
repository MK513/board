package kkmm.back.board.web.controller;

import kkmm.back.board.ControllerTestSupport;
import kkmm.back.board.domain.model.Category;
import kkmm.back.board.domain.model.Member;
import kkmm.back.board.web.SessionConst;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BoardControllerTest extends ControllerTestSupport {

    @Test
    void 게시글_목록_페이지() throws Exception {
        Member member = joinMember("board@test.com", "1", "board");
        Category category = createCategory("cat");
        saveSampleNotes(category, member);

        mockMvc.perform(get("/board/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("form/listForm"))
                .andExpect(model().attributeExists("notes", "totalPages", "categoryForm"));
    }

    @Test
    void 게시글_검색_페이지() throws Exception {
        Member member = joinMember("board2@test.com", "1", "board");
        Category category = createCategory("cat2");
        saveSampleNotes(category, member);

        mockMvc.perform(get("/board/search")
                        .sessionAttr(SessionConst.LOGIN_MEMBER, member)
                        .param("searchType", "title")
                        .param("keyword", "1")
                        .param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("form/listForm"));
    }
}
