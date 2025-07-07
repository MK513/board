package kkmm.back.board.domain.Service;

import jakarta.validation.constraints.NotEmpty;
import kkmm.back.board.domain.model.Note;
import kkmm.back.board.domain.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;

    @Transactional
    public Long saveNote(Note note) {
        noteRepository.save(note);
        return note.getId();
    }

    public Note findOne(Long id) {
        return noteRepository.findOne(id);
    }

    public List<Note> findPage(int page) {
        return noteRepository.findNoteRange((page - 1) * 10, 10);
    }

    public List<Note> searchPage(int page, String keyword, String searchType) {
        return noteRepository.searchNotesRange(searchType, keyword, (page - 1) * 10, 10);
    }

    public Long findNoteCount() {
        return noteRepository.findNoteCount();
    }

    public Long searchNoteCount(String keyword, String searchType) {
        return noteRepository.searchNoteCount(keyword, searchType);
    }

    @Transactional
    public void increaseViewCount(Long id) {
        noteRepository.increaseViewCount(id);
    }

    @Transactional
    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }

    @Transactional
    public void updateNote(Long id, @NotEmpty String title, @NotEmpty String content) {
        Note note = noteRepository.findOne(id);
        note.updateContent(title, content);
        noteRepository.save(note);
    }
}
