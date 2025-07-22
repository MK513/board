package kkmm.back.board.web.controller;

import jakarta.transaction.Transactional;
import kkmm.back.board.ControllerTestSupport;
import kkmm.back.board.domain.model.Category;
import kkmm.back.board.domain.model.Member;
import kkmm.back.board.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        categoryRepository.save(new Category("default"));

        mockMvc.perform(post("/category/create")
                        .sessionAttr(SessionConst.LOGIN_MEMBER, member)
                        .param("name", "new"))
                .andExpect(status().isOk())
                .andExpect(view().name("form/manageCategoryForm"));

        Category savedCategory = categoryService.findByName("new");
    }

    @Test
    void 카테고리_중복_생성() throws Exception {
        Member member = joinMember("cat2@test.com", "1", "name");
        categoryRepository.save(new Category("default"));
        categoryRepository.save(new Category("duplicate"));

        mockMvc.perform(post("/category/create")
                        .sessionAttr(SessionConst.LOGIN_MEMBER, member)
                        .param("name", "duplicate"))
                .andExpect(status().isOk())
                .andExpect(view().name("form/manageCategoryForm"))
                .andExpect(model().attributeHasFieldErrors("categoryForm"));

        assertThat(categoryRepository.count()).isEqualTo(2);
    }

    @Test
    void 카테고리_생성_이름_길이_제한() throws Exception {
        Member member = joinMember("cat2@test.com", "1", "name");

        mockMvc.perform(post("/category/create")
                        .sessionAttr(SessionConst.LOGIN_MEMBER, member)
                        .param("name", "LONGESETWORLDOVERTHEWORLD"))
                .andExpect(status().isOk())
                .andExpect(view().name("form/manageCategoryForm"))
                .andExpect(model().attributeHasFieldErrors("categoryForm", "name"));

        assertThat(categoryRepository.count()).isEqualTo(0);
    }

    @Test
    void 카테고리_제거() throws Exception {
        Member member = joinMember("cat2@test.com", "1", "name");

        Long firstId = categoryRepository.save(new Category("default")).getId();
        Long secondId = categoryRepository.save(new Category("two")).getId();

        mockMvc.perform(delete("/category/remove")
                        .sessionAttr(SessionConst.LOGIN_MEMBER, member)
                        .param("name", "two")
                        .param("id", secondId.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("form/manageCategoryForm"));

        assertThatThrownBy(() ->categoryService.findById(secondId))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 기본_카테고리_제거_실패() throws Exception {
        Member member = joinMember("cat2@test.com", "1", "name");

        String defaultName = "자유게시판";
        Long savedId = categoryRepository.save(new Category(defaultName)).getId();

        mockMvc.perform(delete("/category/remove")
                        .sessionAttr(SessionConst.LOGIN_MEMBER, member)
                        .param("name", defaultName)
                        .param("id", savedId.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("form/manageCategoryForm"))
                .andExpect(model().attributeHasFieldErrors("categoryForm", "name"));

        String category = categoryService.findById(savedId).getName();
        assertThat(category).isEqualTo(defaultName);
    }
}
