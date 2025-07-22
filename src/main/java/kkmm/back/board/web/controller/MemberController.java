package kkmm.back.board.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kkmm.back.board.domain.Service.MemberService;
import kkmm.back.board.domain.dto.SignupDto;
import kkmm.back.board.domain.model.Comment;
import kkmm.back.board.domain.model.Member;
import kkmm.back.board.domain.model.Note;
import kkmm.back.board.web.SessionConst;
import kkmm.back.board.web.argumentResolver.Login;
import kkmm.back.board.web.dto.CommentForm;
import kkmm.back.board.web.dto.LoginForm;
import kkmm.back.board.web.dto.NoteForm;
import kkmm.back.board.web.dto.SignupForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원가입
     */
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

        memberService.join(new SignupDto(signupForm));

        return "redirect:/board/list";
    }

    /**
     * 로그인
     */

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("login", new LoginForm());
        return "member/loginForm";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute("login") LoginForm loginForm, BindingResult bindingResult,
                        @RequestParam(defaultValue = "/") String redirectURL, HttpServletRequest request) {

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

        log.info("loginMember={}", loginMember);
        log.info("redirectURL = {}", redirectURL);

        return "redirect:" + redirectURL;
    }

    /**
     * 로그아웃
     */

    @PostMapping("/logout")
    public String logout(HttpSession session) {

        if (session != null) {
            session.invalidate();
        }
        return "redirect:/board/list";
    }

    /**
     * 개인 페이지
     */
    @GetMapping("/manage")
    public String memberManageForm(@Login Member loginMember,
                                   @RequestParam(required = false, defaultValue = "1", value = "notePage") int notePage,
                                   @RequestParam(required = false, defaultValue = "1", value = "commentPage") int commentPage,
                                   Model model) {

        List<NoteForm> notes = memberService.findNotes(loginMember.getId(), notePage, 10).getContent()
                .stream().map(NoteForm::new).toList();
        List<CommentForm> comments = memberService.findComments(loginMember.getId(), commentPage, 10).getContent()
                .stream().map(CommentForm::new).toList();

        int totalNotesCount = memberService.countNotes(loginMember.getId());
        int totalCommentsCount = memberService.countComments(loginMember.getId());

        log.info("totalNotePages={}", totalNotesCount);
        log.info("totalCommentPages={}", totalCommentsCount);

        model.addAttribute("noteCurPage", notePage);
        model.addAttribute("commentCurPage", commentPage);
        model.addAttribute("totalNotesCount", totalNotesCount);
        model.addAttribute("totalCommentsCount", totalCommentsCount);
        model.addAttribute("memberNotes", notes);
        model.addAttribute("memberComments", comments);

        return "form/manageMemberForm";
    }
}
