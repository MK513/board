package kkmm.back.board.domain.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import kkmm.back.board.domain.model.Note;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {

    @Modifying
    @Query("update Note n set n.viewCount = n.viewCount + 1 where n.id = :id")
    public void increaseViewCount(@Param("id") Long id);

}
