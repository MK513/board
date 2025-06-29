package kkmm.back.board.domain.repositoy;

import jakarta.persistence.EntityManager;
import kkmm.back.board.domain.model.Note;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
// TODO JPA REPOSITORY로 추후 업데이트 예정
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

    public void increaseViewCount(Long id) {
        em.createQuery("update Note n set n.viewCount = n.viewCount + 1 where n.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    public List<Note> findNoteRange(int from, int count) {
        return em.createQuery("select n from Note n order by n.createdAt DESC", Note.class)
                .setFirstResult(from)
                .setMaxResults(count)
                .getResultList();
    }

    public Long findNoteCount() {
        return em.createQuery("select count(n) from Note n", Long.class)
                .getSingleResult();
    }
}
