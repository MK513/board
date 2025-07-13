package kkmm.back.board.domain.repository;

import kkmm.back.board.domain.model.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoteRepository extends JpaRepository<Note, Long> {

    @Modifying
    @Query("update Note n set n.viewCount = n.viewCount + 1 where n.id = :id")
    public void increaseViewCount(@Param("id") Long id);

    public int countByMemberId(Long memberId);

    public Page<Note> findByMemberId(Long memberId, Pageable pageable);
}
