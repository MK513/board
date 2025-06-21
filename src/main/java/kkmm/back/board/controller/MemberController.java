package kkmm.back.board.controller;

import kkmm.back.board.Service.MemberService;
import kkmm.back.board.domain.Member;
import kkmm.back.board.domain.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("memberDTO", new MemberDTO());
        return "member/signupForm";
    }

    @PostMapping("/signup")
    public String signup(MemberDTO memberDTO) {
        Member member = new Member(memberDTO);

        memberService.save(member);

        return "redirect:/board/list";
    }
}
