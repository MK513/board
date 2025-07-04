package kkmm.back.board;

import jakarta.annotation.PostConstruct;
import kkmm.back.board.domain.Service.CategoryService;
import kkmm.back.board.domain.Service.MemberService;
import kkmm.back.board.domain.Service.NoteService;
import kkmm.back.board.domain.model.Category;
import kkmm.back.board.domain.model.Member;
import kkmm.back.board.domain.model.Note;
import kkmm.back.board.domain.repository.MemberRepository;
import kkmm.back.board.domain.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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

        private final MemberService memberService;
        private final NoteService noteService;
        private final CategoryService categoryService;

        private final Random random = new Random();

        @Transactional
        public void initData() {
            // 1) 회원 생성
            Long alice1 = memberService.join("Alice", "alice@example.com", "alicePass1!");
            Long bob1 = memberService.join("Bob", "123", "123");

            Member alice = memberService.findById(alice1);
            Member bob = memberService.findById(bob1);

            Category category = categoryService.findOne(1L);

            // 2) 샘플 제목과 내용 목록
            List<String> titles = Arrays.asList(
                    "Spring Boot 시작하기",
                    "JPA와 엔티티 매핑 깊게 이해하기",
                    "Thymeleaf 프래그먼트 활용 팁",
                    "REST API 설계 베스트 프랙티스",
                    "Java Stream 활용 사례",
                    "MySQL 인덱스 튜닝 가이드",
                    "Docker 컨테이너 최적화 방법",
                    "JUnit5로 단위 테스트 작성하기",
                    "Git 브랜치 전략 비교",
                    "AWS EC2 배포 자동화"
            );
            List<String> contents = Arrays.asList(
                    "이 글에서는 Spring Boot 프로젝트를 처음 세팅하는 방법을 자세히 다룹니다.\n필요한 의존성부터 실행까지 순서대로 따라와 보세요.",
                    "JPA의 @OneToMany, @ManyToOne 관계 매핑을 실제 예시와 함께 살펴봅니다.\n성능 최적화 팁도 함께 제공합니다.",
                    "Thymeleaf에서 레이아웃과 프래그먼트를 효율적으로 관리하는 방법을 정리했습니다.\n재사용 가능한 템플릿 구조를 설계해 보세요.",
                    "REST API를 설계할 때 URI, HTTP 메서드, 응답 형식을 어떻게 정할지 고민된다면 이 글을 참고하세요.\nHATEOAS 예시도 포함되어 있습니다.",
                    "Java Stream API로 리스트 필터링, 매핑, 집계 기능을 손쉽게 구현하는 다양한 예제를 모아봤습니다."
            );

            LocalDateTime baseTime = LocalDateTime.now();

            // 3) 노트 20개 저장 (Alice/Bob 번갈아가며)
            for (int i = 0; i < 256; i++) {
                Member author = (i % 2 == 0) ? alice : bob;
                String title   = titles.get(i % titles.size());
                String content = contents.get(i % contents.size());

                Note note = new Note(author, title, content, category);

                // 랜덤하게 -0~9일, -0~23시간, -0~59분 차이 적용
                LocalDateTime randomTime = baseTime
                        .minusDays(random.nextInt(10))
                        .minusHours(random.nextInt(24))
                        .minusMinutes(random.nextInt(60));
                note.setCreatedAt(randomTime);

                noteService.saveNote(note);
            }

        }
    }
}
