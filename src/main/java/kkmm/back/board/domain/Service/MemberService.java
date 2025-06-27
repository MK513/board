package kkmm.back.board.domain.Service;

import kkmm.back.board.domain.model.Member;
import kkmm.back.board.domain.repositoy.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long save(Member member) {

        validateDuplicateMember(member);

        memberRepository.save(member);

        return member.getId();
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .getFirst();
    }
    
    
    public Member login(String email, String password) {
        return memberRepository.findByEmail(email)
                .stream().filter(m -> m.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }


    private void validateDuplicateMember(Member member) {
        List<Member> findMember = memberRepository.findByEmail(member.getEmail());
        if (!findMember.isEmpty()) {
            throw new IllegalStateException("이미 사용중인 이메일 입니다.");
        }
    }
}
