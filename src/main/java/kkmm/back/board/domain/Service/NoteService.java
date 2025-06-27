package kkmm.back.board.domain.Service;

import kkmm.back.board.domain.model.Note;
import kkmm.back.board.domain.repositoy.NoteRepository;
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
    public void saveNote(Note note) {
        noteRepository.save(note);
    }

    public Note findOne(Long id) {
        return noteRepository.findOne(id);
    }

    public List<Note> findNotes() {
        return noteRepository.findAll();
    }

    @Transactional
    public void increaseViewCount(Long id) {
        noteRepository.increaseViewCount(id);
    }
}
