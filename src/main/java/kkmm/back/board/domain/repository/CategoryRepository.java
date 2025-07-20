package kkmm.back.board.domain.repository;

import kkmm.back.board.domain.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    public List<Category> findByNameEquals(String name);

//    @Modifying
//    @Query("update Category c set c.count = c.count + 1 where c.id = :id")
//    public void increaseCount(@Param("id") Long id);
//
//    @Modifying
//    @Query("update Category c set c.count = c.count - 1 where c.id = :id")
//    public void decreaseCount(@Param("id") Long id);
}
