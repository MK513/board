package kkmm.back.board.domain.Service;

import kkmm.back.board.domain.model.Category;
import kkmm.back.board.domain.model.Member;
import kkmm.back.board.domain.repositoy.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    @Transactional
    public void increaseCount(Category category) {
        categoryRepository.increaseCount(category.getName());
    }

    public Category findOne(Long id) {
        return categoryRepository.findOne(id);
    }

    public Category findByName(String name) {
        return categoryRepository.findByName(name)
                .stream().findFirst()
                .orElse(null);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    private void validateDuplicateMember(Category category) {
        List<Category> findCategory = categoryRepository.findByName(category.getName());
        if (!findCategory.isEmpty()) {
            throw new IllegalStateException("이미 사용중인 이메일 입니다.");
        }
    }
}
