package kkmm.back.board.web.controller;

import kkmm.back.board.ControllerTestSupport;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class HomeControllerTest extends ControllerTestSupport {

    @Test
    void 루트_리다이렉트() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/board/list"));
    }
}
