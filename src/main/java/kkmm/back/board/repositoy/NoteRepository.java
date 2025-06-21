package kkmm.back.board.repositoy;

import jakarta.persistence.EntityManager;
import kkmm.back.board.domain.Note;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NoteRepository {

    private final EntityManager em;

    public void save(Note note) {
        em.persist(note);
    }

    public Note findOne(Long id) {
        return em.find(Note.class, id);
    }

    public List<Note> findAll() {
        return em.createQuery("select n from Note n", Note.class)
                .getResultList();
    }
}
