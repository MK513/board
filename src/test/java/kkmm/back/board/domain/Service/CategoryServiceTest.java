package kkmm.back.board.domain.Service;

import jakarta.persistence.EntityManager;
import kkmm.back.board.domain.dto.CategoryDto;
import kkmm.back.board.domain.model.Category;
import kkmm.back.board.IntegrationTestSupport;
import kkmm.back.board.domain.Service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CategoryServiceTest extends IntegrationTestSupport {

    @Test
    public void 카테고리_저장() throws Exception {
        //given
        CategoryDto category = new CategoryDto("dummy");

        //when
        Long savedId = categoryService.save(category);

        //then
        Category findCategory = categoryService.findById(savedId);

        assertThat(findCategory.getName()).isEqualTo(category.getName());
    }

    @Test
    public void 카테고리_저장_실패() throws Exception {
        //given
        CategoryDto category = new CategoryDto("dummy");
        CategoryDto dupCategory = new CategoryDto("dummy");

        //when
        categoryService.save(category);

        //then
        assertThatThrownBy(() -> categoryService.save(dupCategory))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void 카테고리_제거() throws Exception {
        //given
        CategoryDto category = new CategoryDto("dummy");
        Long savedId = categoryService.save(category);

        //when
        categoryService.remove(savedId);

        //then
        assertThatThrownBy(() -> categoryService.findById(savedId))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void 카테고리_이름_검색() throws Exception {
        //given
        CategoryDto category = new CategoryDto("dummy");
        categoryService.save(category);

        //when
        Category findCategory = categoryService.findByName("dummy");

        //then
        assertThat(findCategory.getName()).isEqualTo(category.getName());
        assertThat(categoryService.findByName("dummy2")).isNull();
    }

    @Test
    public void 카테고리_모두_찾기() throws Exception {
        //given
        CategoryDto category1 = new CategoryDto("dummy1");
        CategoryDto category2 = new CategoryDto("dummy2");
        CategoryDto category3 = new CategoryDto("dummy3");

        //when
        categoryService.save(category1);
        categoryService.save(category2);
        categoryService.save(category3);

        //then
        List<Category> categories = categoryService.findAll();
        assertThat(categories.size()).isEqualTo(3);
        assertThat(categories.get(0).getName()).isEqualTo(category1.getName());
        assertThat(categories.get(1).getName()).isEqualTo(category2.getName());
        assertThat(categories.get(2).getName()).isEqualTo(category3.getName());
    }
}