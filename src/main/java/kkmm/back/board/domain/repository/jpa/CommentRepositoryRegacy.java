package kkmm.back.board.domain.repository.jpa;

import jakarta.persistence.EntityManager;
import kkmm.back.board.domain.model.Comment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CommentRepositoryRegacy {

    private final EntityManager em;

    public void save(Comment comment) {
        em.persist(comment);
    }

    public Comment findOne(Long id) {
        return em.find(Comment.class, id);
    }

    public List<Comment> findCommentsByNoteId(Long noteId) {
        return em.createQuery("select c from Comment c where c.note.id = :noteId order by c.seq, c.depth, c.createdAt", Comment.class)
                .setParameter("noteId", noteId)
                .getResultList();
    }

    public void deleteById(Long id) {
        Comment comment = em.find(Comment.class, id);
        if (comment != null) em.remove(comment);
    }
}
