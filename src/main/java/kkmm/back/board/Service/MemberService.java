package kkmm.back.board.Service;

import kkmm.back.board.domain.Member;
import kkmm.back.board.repositoy.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    private void validateDuplicateMember(Member member) {
        List<Member> findMember = memberRepository.findByEmail(member.getEmail());
        if (!findMember.isEmpty()) {
            throw new IllegalStateException("이미 사용중인 이메일 입니다.");
        }
    }
}
