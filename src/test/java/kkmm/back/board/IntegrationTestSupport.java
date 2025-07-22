package kkmm.back.board;

import kkmm.back.board.domain.Service.*;
import kkmm.back.board.domain.dto.CategoryDto;
import kkmm.back.board.domain.dto.CommentDto;
import kkmm.back.board.domain.dto.SignupDto;
import kkmm.back.board.domain.model.*;
import kkmm.back.board.domain.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Transactional
public abstract class IntegrationTestSupport {

    @Autowired protected MemberService memberService;
    @Autowired protected CategoryService categoryService;
    @Autowired protected CommentService commentService;
    @Autowired protected NoteService noteService;

    @Autowired protected NoteRepository noteRepository;
    @Autowired protected MemberRepository memberRepository;
    @Autowired protected CategoryRepository categoryRepository;
    @Autowired protected CommentRepository commentRepository;
    @Autowired protected NoteQueryRepository noteQueryRepository;

    protected Member joinMember(String email, String password, String name) {
        SignupDto dto = new SignupDto(email, password, name);
        Long id = memberService.join(dto);
        return memberService.findById(id);
    }

    protected Category createCategory(String name) {
        Long id = categoryService.save(new CategoryDto(name));
        return categoryService.findById(id);
    }

    protected List<Note> saveSampleNotes(Category category, Member member) {
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
        for (int i = 0; i < notes.size(); i++) {
            notes.get(i).setCreatedAt(baseTime.plusDays(i));
        }
        noteRepository.saveAll(notes);
        return notes;
    }

    protected void saveSampleComments(Member member, List<Note> notes) throws InterruptedException {
        for (int i = notes.size() - 1; i >= 0; i--) {
            CommentDto dto = new CommentDto();
            dto.setNoteId(notes.get(i).getId());
            commentService.save(dto, member);
            Thread.sleep(10);
        }
    }
}
