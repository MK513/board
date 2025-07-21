package kkmm.back.board.domain.repository.jpa;

import jakarta.persistence.EntityManager;
import kkmm.back.board.domain.model.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryLegacy {

    private final EntityManager em;
    private final NoteRepositoryLegacy noteRepository;

    public void save(Category category) {
        em.persist(category);
    }

    public void deleteById(Long id) {
        Category category = em.find(Category.class, id);
        if (category != null) em.remove(category);
    }

    public Category findOne(Long id) {
        return em.find(Category.class, id);
    }

    public List<Category> findAll() {
        return em.createQuery("select c from Category c", Category.class)
                .getResultList();
    }

    public List<Category> findByName(String name) {
        return em.createQuery("select c from Category c where c.name = :name", Category.class)
                .setParameter("name", name)
                .getResultList();
    }

    public void increaseCount(String name) {
        em.createQuery("update Category c set c.count = c.count + 1 where c.name = : name")
                .setParameter("name", name)
                .executeUpdate();
    }

    public void decreaseCount(String name) {
        em.createQuery("update Category c set c.count = c.count - 1 where c.name = : name")
                .setParameter("name", name)
                .executeUpdate();
    }
}
