package kkmm.back.board.web.controller;

import kkmm.back.board.ControllerTestSupport;
import kkmm.back.board.domain.model.Member;
import kkmm.back.board.web.SessionConst;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CategoryControllerTest extends ControllerTestSupport {

    @Test
    void 카테고리_관리_페이지() throws Exception {
        Member member = joinMember("cat@test.com", "1", "name");

        mockMvc.perform(get("/category/manage")
                        .sessionAttr(SessionConst.LOGIN_MEMBER, member))
                .andExpect(status().isOk())
                .andExpect(view().name("form/manageCategoryForm"))
                .andExpect(model().attributeExists("categories", "categoryForm"));
    }

    @Test
    void 카테고리_생성() throws Exception {
        Member member = joinMember("cat2@test.com", "1", "name");

        mockMvc.perform(post("/category/create")
                        .sessionAttr(SessionConst.LOGIN_MEMBER, member)
                        .param("name", "new"))
                .andExpect(status().isOk())
                .andExpect(view().name("form/manageCategoryForm"));
    }
}
