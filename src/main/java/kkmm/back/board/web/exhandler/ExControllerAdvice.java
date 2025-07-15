package kkmm.back.board.web.exhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@Slf4j
@ControllerAdvice
public class ExControllerAdvice {

    @ExceptionHandler(IllegalStateException.class)
    public String handleIllegalStateException(IllegalStateException e, Model model) {
        log.error("IllegalStateException", e);
        model.addAttribute("errorMessage", e.getMessage());
        return "error/oc";
    }

    @ExceptionHandler(BadCredentialsException.class)
    public String handleBadCredentialsException(BadCredentialsException e, Model model) {
        log.error("BadCredentialsException", e);
        model.addAttribute("errorMessage", e.getMessage());
        return "error/oc";
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public String handleUsernameNotFoundException(UsernameNotFoundException e, Model model) {
        log.error("UsernameNotFoundException", e);
        model.addAttribute("errorMessage", e.getMessage());
        return "error/oc";
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException e, Model model) {
        log.error("NoSuchElementException", e);
        model.addAttribute("errorMessage", e.getMessage());
        return "error/oc";
    }

}
