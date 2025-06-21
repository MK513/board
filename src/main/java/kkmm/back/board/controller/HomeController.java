package kkmm.back.board.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class HomeController {

    @GetMapping("/")
    public String redirectToBoard() {
        log.info("redirect");
        return "redirect:/board/list";
    }
}
