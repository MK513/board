package kkmm.back.board;

import jakarta.annotation.PostConstruct;
import kkmm.back.board.domain.model.Member;
import kkmm.back.board.domain.model.Note;
import kkmm.back.board.repositoy.MemberRepository;
import kkmm.back.board.repositoy.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.initData();
    }

    @Component
    @RequiredArgsConstructor
    static class InitService {

        private final MemberRepository memberRepository;
        private final NoteRepository noteRepository;

        @Transactional
        public void initData() {
            Member member = new Member("abcd@test.com", "123!", "TEST");
            Member GINI = new Member("123", "123", "GINI");
            memberRepository.save(member);
            memberRepository.save(GINI);

            noteRepository.save(new Note(member, "Title1", "contents1"));
            noteRepository.save(new Note(member, "Title2", "contents2"));
            noteRepository.save(new Note(member, "Title3", "contents3"));
        }
    }
}
