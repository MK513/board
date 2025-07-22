package kkmm.back.board.web.controller;

import kkmm.back.board.ControllerTestSupport;
import kkmm.back.board.web.SessionConst;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MemberControllerTest extends ControllerTestSupport {

    @Test
    void 로그인_폼() throws Exception {
        mockMvc.perform(get("/member/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("member/loginForm"));
    }

    @Test
    void 회원가입() throws Exception {
        mockMvc.perform(post("/member/signup")
                        .param("email", "new@test.com")
                        .param("password", "pw")
                        .param("name", "name"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/board/list"));
    }

    @Test
    void 로그인() throws Exception {
        joinMember("user@test.com", "pw", "name");

        mockMvc.perform(post("/member/login")
                        .param("email", "user@test.com")
                        .param("password", "pw")
                        .param("redirectURL", "/board/list"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/board/list"))
                .andExpect(request().sessionAttribute(SessionConst.LOGIN_MEMBER, notNullValue()));
    }
}
