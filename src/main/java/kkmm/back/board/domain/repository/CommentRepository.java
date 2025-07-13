package kkmm.back.board.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import kkmm.back.board.domain.model.Comment;
import kkmm.back.board.domain.model.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c where c.note.id = :noteId order by c.seq, c.depth, c.createdAt")
    public List<Comment> findAllByNoteId(@Param("noteId") Long noteId);

    public Page<Comment> findByMemberId(Long memberId, Pageable pageable);

    public int countByMemberId(Long memberId);

}
