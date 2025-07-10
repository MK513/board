package kkmm.back.board.domain.repository.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import kkmm.back.board.domain.model.Note;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
// TODO JPA REPOSITORY로 추후 업데이트 예정
public class NoteRepositoryRegacy {

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

    // TODO 대소문자 통합 검색은 추후 JPA, querydsl 사용해서 구현 예정
    public List<Note> searchNotesRange(String searchType, String keyword, int from, int count) {
        // 1. 기본 SELECT 절
        StringBuilder jpql = new StringBuilder("select n from Note n");

        // 2. 검색 키워드가 있으면 WHERE 절 추가
        if (keyword != null && !keyword.isBlank()) {
            jpql.append(" where ");
            switch (searchType) {
                case "author":
                    jpql.append("n.member.name like :kw");
                    break;
                case "title":
                    jpql.append("n.title like :kw");
                    break;
                case "content":
                    jpql.append("n.content like :kw");
                    break;
                case "title_content":
                    jpql.append("(n.title like :kw or n.content like :kw)");
                    break;
                default:
                    // searchType 값이 없거나 잘못 들어왔을 때 전체 검색
                    jpql.append("(n.title like :kw or n.content like :kw or n.member.name like :kw)");
            }
        }

        // 3. 정렬 절
        jpql.append(" order by n.createdAt desc");

        // 4. TypedQuery 생성
        TypedQuery<Note> query = em.createQuery(jpql.toString(), Note.class);

        // 5. 파라미터 바인딩
        if (keyword != null && !keyword.isBlank()) {
            query.setParameter("kw", "%" + keyword.trim() + "%");
        }

        // 6. 페이징
        query.setFirstResult(from);
        query.setMaxResults(count);

        return query.getResultList();
    }

    public Long findNoteCount() {
        return em.createQuery("select count(n) from Note n", Long.class)
                .getSingleResult();
    }

    public Long searchNoteCount(String keyword, String searchType) {

        // 1. 기본 SELECT 절
        StringBuilder jpql = new StringBuilder("select count(n) from Note n");

        // 2. 검색 키워드가 있으면 WHERE 절 추가
        if (keyword != null && !keyword.isBlank()) {
            jpql.append(" where ");
            switch (searchType) {
                case "author":
                    jpql.append("n.member.name like :kw");
                    break;
                case "title":
                    jpql.append("n.title like :kw");
                    break;
                case "content":
                    jpql.append("n.content like :kw");
                    break;
                case "title_content":
                    jpql.append("(n.title like :kw or n.content like :kw)");
                    break;
                default:
                    // searchType 값이 없거나 잘못 들어왔을 때 전체 검색
                    jpql.append("(n.title like :kw or n.content like :kw or n.member.name like :kw)");
            }
        }

        // 3. TypedQuery 생성
        TypedQuery<Long> query = em.createQuery(jpql.toString(), Long.class);

        // 4. 파라미터 바인딩
        if (keyword != null && !keyword.isBlank()) {
            query.setParameter("kw", "%" + keyword.trim() + "%");
        }

        return query.getSingleResult();
    }

    public void deleteById(Long id) {
        Note note = em.find(Note.class, id);
        if (note != null) em.remove(note);
    }

}
