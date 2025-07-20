package kkmm.back.board.domain.Service;

import jakarta.persistence.EntityManager;
import kkmm.back.board.domain.dto.NoteDto;
import kkmm.back.board.domain.dto.SignupDto;
import kkmm.back.board.domain.model.Category;
import kkmm.back.board.domain.model.Member;
import kkmm.back.board.domain.model.Note;
import kkmm.back.board.domain.repository.NoteRepository;
import kkmm.back.board.web.dto.NoteForm;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
class NoteServiceTest {

    @Autowired NoteService noteService;
    @Autowired MemberService memberService;
    @Autowired NoteRepository noteRepository;
    @Autowired CategoryService categoryService;

    @Test
    public void 게시글_저장() throws Exception {
        //given
        Member member = joinMember("a", "1", "2");
        Category category = makeCategory();
        NoteDto noteDto = new NoteDto();
        noteDto.setTitle("title");
        noteDto.setContents("content");

        //when
        Long savedId = noteService.save(noteDto, member, category);
        Note findNote = noteService.findById(savedId);

        //then
        assertThat(findNote.getTitle()).isEqualTo("title");
        assertThat(findNote.getContent()).isEqualTo("content");
        assertThat(findNote.getCategory().getCount()).isEqualTo(1);
    }

    @Test
    public void 게시글_페이지_받아오기() throws Exception {
        //given
        Member member = joinMember("a", "1", "2");
        Category category = makeCategory();
        save_sample_notes(category, member);

        //when
        List<Note> notes = noteService.findNotes(1);

        //then
        assertThat(notes.size()).isEqualTo(10);
        assertThat(notes.get(0).getTitle()).isEqualTo("10");
        assertThat(notes.get(9).getTitle()).isEqualTo("1");
    }

    @Test
    public void 검색_게시글_페이지_받아오기() throws Exception {
        //given
        Member member = joinMember("a", "a", "a");
        Category category = makeCategory();
        save_sample_notes(category, member);

        //when
        List<Note> searchAuthor = noteService.searchNotes(1, "a" ,"author");
        List<Note> searchTitle = noteService.searchNotes(1, "1", "title");
        List<Note> searchContent = noteService.searchNotes(1, "2", "content");
        List<Note> searchTitleContent = noteService.searchNotes(1, "3", "title_content");

        //then
        assertThat(searchAuthor.size()).isEqualTo(10);

        assertThat(searchTitle.size()).isEqualTo(2);
        assertThat(searchTitle.get(0).getTitle()).isEqualTo("10");
        assertThat(searchTitle.get(1).getTitle()).isEqualTo("1");

        assertThat(searchContent.get(0).getContent()).isEqualTo("2");
        assertThat(searchTitleContent.get(0).getTitle()).isEqualTo("3");
    }
    
    @Test
    public void 게시글_업데이트() throws Exception {
        //given
        Member member = joinMember("a", "1", "2");
        Category category = makeCategory();
        NoteDto noteDto = new NoteDto();
        noteDto.setTitle("title");
        noteDto.setContents("content");
        Long savedId = noteService.save(noteDto, member, category);

        //when
        NoteDto updateNoteDto = new NoteDto();
        updateNoteDto.setTitle("updateTitle");
        updateNoteDto.setContents("updateContent");
        noteService.updateNote(savedId, updateNoteDto);

        Note updateNote = noteService.findById(savedId);

        //then
        assertThat(updateNote.getTitle()).isEqualTo("updateTitle");
        assertThat(updateNote.getContent()).isEqualTo("updateContent");
    }

    private Member joinMember(String email, String password, String name) throws Exception {
        SignupDto signupDto = new SignupDto(email, password, name);
        Long joinId = memberService.join(signupDto);
        return memberService.findById(joinId);
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

    private Category makeCategory() {
        Long categoryId = categoryService.save(new Category("default"));
        return categoryService.findById(categoryId);
    }
}