package kkmm.back.board.domain.Service;

import kkmm.back.board.IntegrationTestSupport;
import kkmm.back.board.domain.dto.NoteDto;
import kkmm.back.board.domain.model.Category;
import kkmm.back.board.domain.model.Member;
import kkmm.back.board.domain.model.Note;
import kkmm.back.board.web.dto.NoteForm;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class NoteServiceTest extends IntegrationTestSupport {


    @Test
    public void 게시글_저장() throws Exception {
        //given
        Member member = joinMember("a", "1", "2");
        Category category = createCategory("default");
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
        Category category = createCategory("default");
        saveSampleNotes(category, member);

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
        Category category = createCategory("default");
        saveSampleNotes(category, member);

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
        Category category = createCategory("default");
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


}