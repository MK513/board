package kkmm.back.board.domain.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import kkmm.back.board.domain.model.Note;
import kkmm.back.board.domain.model.QNote;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kkmm.back.board.domain.model.QNote.*;

@Repository
public class NoteQueryRepository {

    EntityManager em;
    JPAQueryFactory query;

    public NoteQueryRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    public List<Note> findNoteRange(int from, int count) {
        return query.select(note)
                .from(note)
                .orderBy(note.createdAt.desc())
                .offset(from)
                .limit(count)
                .fetch();
    }

    public List<Note> searchNotesRange(String searchType, String keyword, int from, int count) {
        return query.select(note)
                .from(note)
                .where(searchCond(searchType, keyword))
                .orderBy(note.createdAt.desc())
                .offset(from)
                .limit(count)
                .fetch();
    }

    public Long countSearchedNotes(String keyword, String searchType) {
        return query.select(note.count())
                .from(note)
                .where(searchCond(searchType, keyword))
                .fetchOne();
    }

    private BooleanExpression searchCond(String searchType, String keyword) {
        if (searchType == null || searchType.isBlank()) return null;
        if (keyword == null || keyword.isBlank()) return null;

        return switch (searchType) {
            case "author" -> note.member.name.containsIgnoreCase(keyword);
            case "title" -> note.title.containsIgnoreCase(keyword);
            case "content" -> note.content.containsIgnoreCase(keyword);
            case "title_content" -> note.title.containsIgnoreCase(keyword).or(note.content.containsIgnoreCase(keyword));
            default -> null;
        };
    }


}
