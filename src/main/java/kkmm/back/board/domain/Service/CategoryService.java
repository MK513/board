package kkmm.back.board.domain.Service;

import kkmm.back.board.domain.model.Category;
import kkmm.back.board.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
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
        validateDuplicateMember(category);

        categoryRepository.save(category);

        return category.getId();
    }

    @Transactional
    public void remove(Long id) {
        categoryRepository.deleteById(id);
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id).orElseThrow();
    }

    public Category findByName(String name) {
        return categoryRepository.findByNameLike(name)
                .stream().findFirst()
                .orElse(null);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    private void validateDuplicateMember(Category category) {
        List<Category> findCategory = categoryRepository.findByNameLike(category.getName());
        if (!findCategory.isEmpty()) {
            throw new IllegalStateException("이미 사용중인 이메일 입니다.");
        }
    }
}
