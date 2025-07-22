package kkmm.back.board.domain.repository;

import kkmm.back.board.domain.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    public Optional<Category> findByName(String name);

//    @Modifying
//    @Query("update Category c set c.count = c.count + 1 where c.id = :id")
//    public void increaseCount(@Param("id") Long id);
//
//    @Modifying
//    @Query("update Category c set c.count = c.count - 1 where c.id = :id")
//    public void decreaseCount(@Param("id") Long id);
}
