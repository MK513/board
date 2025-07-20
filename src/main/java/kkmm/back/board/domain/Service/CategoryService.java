package kkmm.back.board.domain.Service;

import kkmm.back.board.domain.model.Category;
import kkmm.back.board.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public Long save(Category category) {
        validateDuplicateCategory(category);

        Category saved = categoryRepository.save(category);

        return saved.getId();
    }

    @Transactional
    public void remove(Long id) {
        categoryRepository.deleteById(id);
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id).orElseThrow();
    }

    public Category findByName(String name) {
        return categoryRepository.findByNameEquals(name)
                .stream().findFirst()
                .orElseThrow();
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    private void validateDuplicateCategory(Category category) {
        List<Category> findCategory = categoryRepository.findByNameEquals(category.getName());
        if (!findCategory.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 카테고리입니다.");
        }
    }
}
