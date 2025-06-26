package kkmm.back.board.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kkmm.back.board.Service.MemberService;
import kkmm.back.board.domain.model.Member;
import kkmm.back.board.web.SessionConst;
import kkmm.back.board.web.model.LoginForm;
import kkmm.back.board.web.model.SignupForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("member", new SignupForm());
        return "member/signupForm";
    }

    @PostMapping("/signup")
    public String signup(@Validated @ModelAttribute("member") SignupForm signupForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "member/signupForm";
        }

        log.info("signup");

        Member member = new Member(signupForm);

        memberService.save(member);

        return "redirect:/board/list";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("login", new LoginForm());
        return "member/loginForm";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute("login") LoginForm loginForm, BindingResult bindingResult,
                            @RequestParam(defaultValue = "") String redirectURL, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "member/loginForm";
        }

        Member loginMember = memberService.login(loginForm.getEmail(), loginForm.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "member/loginForm";
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "redirect:/board/list" + redirectURL;
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();

        if (session != null) {
            session.invalidate();
        }

        return "redirect:/";
    }
}
