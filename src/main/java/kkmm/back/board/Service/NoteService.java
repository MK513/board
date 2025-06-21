package kkmm.back.board.Service;

import kkmm.back.board.domain.Note;
import kkmm.back.board.repositoy.NoteRepository;
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
}
