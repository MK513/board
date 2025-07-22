package kkmm.back.board;

import jakarta.transaction.Transactional;
import kkmm.back.board.web.WebConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@Transactional
@AutoConfigureMockMvc
@Import(WebConfig.class)
public abstract class ControllerTestSupport extends IntegrationTestSupport {

    @Autowired
    protected MockMvc mockMvc;
}
