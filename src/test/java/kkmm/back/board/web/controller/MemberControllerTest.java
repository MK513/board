package kkmm.back.board.web.controller;

import kkmm.back.board.ControllerTestSupport;
import kkmm.back.board.domain.model.Member;
import kkmm.back.board.domain.model.Note;
import kkmm.back.board.web.SessionConst;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MemberControllerTest extends ControllerTestSupport {

    @Test
    void 로그인_폼() throws Exception {
        mockMvc.perform(get("/member/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("form/loginForm"));
    }

    @Test
    void 회원가입() throws Exception {
        mockMvc.perform(post("/member/signup")
                        .param("email", "new@test.com")
                        .param("password", "pw123456")
                        .param("name", "name"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/board/list"));
    }

    @Test
    void 회원가입_실패_비밀번호_8자제한() throws Exception {
        mockMvc.perform(post("/member/signup")
                        .param("email", "new@test.com")
                        .param("password", "pw")
                        .param("name", "name"))
                .andExpect(status().isOk())
                .andExpect(view().name("form/signupForm"))
                .andExpect(model().attributeHasFieldErrors("member", "password"));
    }

    @Test
    void 로그인() throws Exception {
        joinMember("user@test.com", "pw123456", "name");

        mockMvc.perform(post("/member/login")
                        .param("email", "user@test.com")
                        .param("password", "pw123456")
                        .param("redirectURL", "/board/list"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/board/list"))
                .andExpect(request().sessionAttribute(SessionConst.LOGIN_MEMBER, notNullValue()));
    }

    @Test
    void 로그인_실패_존재X() throws Exception {
        joinMember("user@test.com", "pw123456", "name");

        mockMvc.perform(post("/member/login")
                        .param("email", "ERRORuser@test.com")
                        .param("password", "pw123456")
                        .param("redirectURL", "/board/list"))
                .andExpect(view().name("error/oc"))
                .andExpect(model().attributeExists("errorMessage"));
    }

    @Test
    void 로그아웃() throws Exception {
        Member member = joinMember("user@test.com", "pw", "name");

        mockMvc.perform(post("/member/logout")
                        .sessionAttr(SessionConst.LOGIN_MEMBER, member))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/board/list"))
                .andExpect(request().sessionAttribute(SessionConst.LOGIN_MEMBER, nullValue()));
    }

    @Test
    void 유저_정보_폼() throws Exception {
        Member member = joinMember("cat@test.com", "1", "name");
        List<Note> notes = saveSampleNotes(createCategory("cat"), member);
        saveSampleComments(member, notes);

        mockMvc.perform(get("/member/manage")
                        .sessionAttr(SessionConst.LOGIN_MEMBER, member))
                .andExpect(status().isOk())
                .andExpect(view().name("form/manageMemberForm"))
                .andExpect(model().attributeExists(
                        "noteCurPage",
                        "commentCurPage",
                        "totalNotesCount",
                        "totalCommentsCount",
                        "memberNotes",
                        "memberComments"));
    }

}
