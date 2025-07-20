package kkmm.back.board.domain.Service;

import kkmm.back.board.domain.dto.CommentDto;
import kkmm.back.board.domain.dto.SignupDto;
import kkmm.back.board.domain.model.Category;
import kkmm.back.board.domain.model.Comment;
import kkmm.back.board.domain.model.Member;
import kkmm.back.board.domain.model.Note;
import kkmm.back.board.domain.repository.CommentRepository;
import kkmm.back.board.domain.repository.NoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@Slf4j
class MemberServiceTest {

    @Autowired NoteRepository noteRepository;
    @Autowired MemberService memberService;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired NoteService noteService;
    @Autowired CommentService commentService;
    @Autowired CommentRepository commentRepository;
    @Autowired CategoryService categoryService;

    @Test
    public void 회원가입() throws Exception {
        //given
        SignupDto signupDto = new SignupDto("a", "b", "c");

        //when
        Long joinId = memberService.join(signupDto);

        //then
        Member findMember = memberService.findById(joinId);

        assertThat(findMember.getName()).isEqualTo(signupDto.getName());
        assertThat(findMember.getEmail()).isEqualTo(signupDto.getEmail());
        assertThat(passwordEncoder.matches(signupDto.getPassword(), findMember.getPassword())).isTrue();
    }

    @Test
    public void 회원가입_실패() throws Exception {
        //given
        Member member = joinMember("a", "1", "2");
        SignupDto dupSignupDto = new SignupDto("a", "1", "2");

        //then
        assertThatThrownBy(() -> memberService.join(dupSignupDto))
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void Pk로_검색실패() throws Exception {
        assertThatThrownBy(() -> memberService.findById(1L))
                .isInstanceOf(NoSuchElementException.class);
    }


    @Test
    void 이메일로_검색실패() throws Exception {
        assertThatThrownBy(() -> memberService.findByEmail("a"))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void 로그인() throws Exception {
        //given
        String email = "a";
        String password = "1";
        Member member = joinMember(email, password, "2");

        //when
        Member loginMember = memberService.login(email, password);

        //then
        assertThat(loginMember.getId()).isEqualTo(member.getId());
    }

    @Test
    public void 로그인_실패_존재하지_않는_유저() throws Exception {
        assertThatThrownBy(() -> memberService.login("a", "c"))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    public void 로그인_실패_비밀번호_불일치() throws Exception {
        //given
        Member member = joinMember("a", "1", "2");

        // when, then
        assertThatThrownBy(() -> memberService.login("a", "c"))
                .isInstanceOf(BadCredentialsException.class);
    }
    
    @Test
    public void 유저별_게시글_검색() throws Exception {
        //given
        Member member = joinMember("a", "1", "2");
        Category category = makeCategory();
        save_sample_notes(category, member);

        //when
        Page<Note> notes = memberService.findNotes(member.getId(), 1, 10);

        //then
        assertThat(notes.getTotalElements()).isEqualTo(10);

        AtomicInteger i = new AtomicInteger(10);
        notes.forEach(note -> {
            assertThat(note.getTitle()).isEqualTo(String.valueOf(i.getAndDecrement()));
        });
    }

    @Test
    public void 유저별_댓글_검색() throws Exception {
        //given
        Member member = joinMember("a", "1", "2");
        Category category = makeCategory();

        save_sample_notes(category, member);
        List<Note> notes = memberService.findNotes(member.getId(), 1, 10).getContent();

        save_sample_comments(member, notes);

        //when
        Page<Comment> comments = memberService.findComments(member.getId(), 1, 10);
//        comments.forEach(comment -> {log.info("comment: {}", comment.getNote().getTitle() + " " + comment.getCreatedAt() );});
//        notes.forEach(note -> {log.info("note: {}", note.getTitle() + " " + note.getCreatedAt() );});

        //then
        assertThat(comments.getTotalElements()).isEqualTo(10);
        AtomicInteger i = new AtomicInteger(0);
        comments.forEach(comment -> {
            assertThat(comment.getNote()).isEqualTo(notes.get(i.getAndIncrement()));
        });
    }

    private Category makeCategory() {
        Long categoryId = categoryService.save(new Category("default"));
        return categoryService.findById(categoryId);
    }

    private Member joinMember(String email, String password, String name) throws Exception {
        SignupDto signupDto = new SignupDto(email, password, name);
        Long joinId = memberService.join(signupDto);
        return memberService.findById(joinId);
    }

    private void save_sample_comments(Member member, List<Note> notes) throws InterruptedException {
        LocalDateTime baseTime = LocalDateTime.now();

        for (int i = notes.size() - 1; i >= 0 ; i--) {
            CommentDto commentDto = new CommentDto();
            commentDto.setNoteId(notes.get(i).getId());
            commentService.save(commentDto, member);

            Thread.sleep(10);
        }
    }

    public void save_sample_notes(Category category, Member member) {
        LocalDateTime baseTime = LocalDateTime.now().minusDays(10);

        List<Note> notes = List.of(
                new Note(member, "1", "1", category),
                new Note(member, "2", "2", category),
                new Note(member, "3", "3", category),
                new Note(member, "4", "4", category),
                new Note(member, "5", "5", category),
                new Note(member, "6", "6", category),
                new Note(member, "7", "7", category),
                new Note(member, "8", "8", category),
                new Note(member, "9", "9", category),
                new Note(member, "10", "10", category)
        );

        for (int i = 0; i < 10; i++) {
            notes.get(i).setCreatedAt(baseTime.plusDays(i));
        }

        noteRepository.saveAll(notes);
    }
}