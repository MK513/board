package kkmm.back.board.domain.repository;

import kkmm.back.board.IntegrationTestSupport;
import kkmm.back.board.domain.model.Category;
import kkmm.back.board.domain.model.Member;
import kkmm.back.board.domain.model.Note;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class NoteQueryRepositoryTest extends IntegrationTestSupport {


    @Test
    public void 게시글_범위_찾기() throws Exception {
        //given
        saveSampleNotes();

        //when
        List<Note> findNotes = noteQueryRepository.findNoteRange(4, 4);

        //then
        assertThat(findNotes.size()).isEqualTo(4);
        assertThat(findNotes.get(0).getTitle()).isEqualTo("3");
        assertThat(findNotes.get(1).getTitle()).isEqualTo("3");
        assertThat(findNotes.get(2).getTitle()).isEqualTo("4");
        assertThat(findNotes.get(3).getTitle()).isEqualTo("4");
    }

    @Test
    public void 게시글_검색_범위_찾기() throws Exception {
        //given
        saveSampleNotes();

        //when
        List<Note> findNotes1 = noteQueryRepository.searchNotesRange("title", "2", 0, 10);

        //then
        assertThat(findNotes1.size()).isEqualTo(2);
        assertThat(findNotes1.get(0).getTitle()).isEqualTo("2a");
        assertThat(findNotes1.get(1).getTitle()).isEqualTo("2b");

        //when
        List<Note> findNotes2 = noteQueryRepository.searchNotesRange("title_content", "5", 0, 10);

        //then
        assertThat(findNotes2.size()).isEqualTo(4);
        assertThat(findNotes2.get(0).getTitle()).isEqualTo("3");
        assertThat(findNotes2.get(1).getTitle()).isEqualTo("3");
        assertThat(findNotes2.get(2).getTitle()).isEqualTo("5");
        assertThat(findNotes2.get(3).getTitle()).isEqualTo("5");
    }

    @Test
    public void 검색된_게시글수_찾기() throws Exception {
        //given
        saveSampleNotes();

        //when
        Long s1 = noteQueryRepository.countSearchedNotes("title", "3" );
        Long s2 = noteQueryRepository.countSearchedNotes("title_content", "5");

        //then
        assertThat(s1).isEqualTo(2L);
        assertThat(s2).isEqualTo(4L);
    }

    private void saveSampleNotes() {
        Category category = createCategory("1");
        Member member1 = joinMember("1", "1", "1");

        LocalDateTime baseTime = LocalDateTime.now();

        List<Note> notes = List.of(
                new Note(member1, "1", "1", category),
                new Note(member1, "1", "1", category),
                new Note(member1, "2a", "2", category),
                new Note(member1, "2b", "2", category),
                new Note(member1, "3", "345", category),
                new Note(member1, "3", "345", category),
                new Note(member1, "4", "4", category),
                new Note(member1, "4", "4", category),
                new Note(member1, "5", "5", category),
                new Note(member1, "5", "5", category)
        );

        for (int i = 0; i < 10; i++) {
            notes.get(i).setCreatedAt(baseTime.minusDays(i));
        }

        noteRepository.saveAll(notes);
    }

}