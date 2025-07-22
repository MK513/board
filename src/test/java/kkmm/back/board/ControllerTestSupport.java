package kkmm.back.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@Transactional
public abstract class ControllerTestSupport extends IntegrationTestSupport {

    @Autowired
    protected MockMvc mockMvc;
}
