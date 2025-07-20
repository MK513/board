package kkmm.back.board.domain.Service;

import jakarta.transaction.Transactional;
import kkmm.back.board.domain.model.Category;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Slf4j
@Transactional
class CategoryServiceTest {

    @Autowired
    CategoryService categoryService;

    @Test
    public void 카테고리_저장() throws Exception {
        //given
        Category category = new Category("dummy");

        //when
        Long savedId = categoryService.save(category);

        //then
        assertThat(savedId).isEqualTo(category.getId());
    }

    @Test
    public void 카테고리_저장_실패() throws Exception {
        //given
        Category category = new Category("dummy");
        Category dupCategory = new Category("dummy");

        //when
        categoryService.save(category);

        //then
        assertThatThrownBy(() -> categoryService.save(dupCategory))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void 카테고리_제거() throws Exception {
        //given
        Category category = new Category("dummy");
        categoryService.save(category);

        //when
        categoryService.remove(category.getId());

        //then
        assertThatThrownBy(() -> categoryService.findById(category.getId()))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void 카테고리_이름_검색() throws Exception {
        //given
        Category category = new Category("dummy");
        categoryService.save(category);

        //when
        Category findCategory = categoryService.findByName("dummy");

        //then
        assertThat(findCategory).isEqualTo(category);
        assertThatThrownBy(() -> categoryService.findByName("dummy2"))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void 카테고리_모두_찾기() throws Exception {
        //given
        Category category1 = new Category("dummy1");
        Category category2 = new Category("dummy2");
        Category category3 = new Category("dummy3");

        //when
        categoryService.save(category1);
        categoryService.save(category2);
        categoryService.save(category3);

        //then
        List<Category> categories = categoryService.findAll();
        assertThat(categories.size()).isEqualTo(3);
        assertThat(categories.get(0)).isEqualTo(category1);
        assertThat(categories.get(1)).isEqualTo(category2);
        assertThat(categories.get(2)).isEqualTo(category3);
    }
}