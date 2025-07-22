package kkmm.back.board.domain.Service;

import kkmm.back.board.domain.dto.CategoryDto;
import kkmm.back.board.domain.model.Category;
import kkmm.back.board.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final MessageSource messageSource;

    @Transactional
    public Long save(CategoryDto categoryDto) {
        Category category = new Category(categoryDto);

        validateDuplicateCategory(category);

        Category saved = categoryRepository.save(category);

        log.info("New category saved {}", saved.getName());

        return saved.getId();
    }

    @Transactional
    public void remove(Long id) {
        categoryRepository.deleteById(id);
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id).orElseThrow( () -> new IllegalStateException(
                messageSource.getMessage("category.error.notFound", null, null)));
    }

    public Category findByName(String name) {
        return categoryRepository.findByName(name).orElse(null);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    private void validateDuplicateCategory(Category category) {
        Locale locale = LocaleContextHolder.getLocale();

        if (categoryRepository.findByName(category.getName()).isPresent()) {
            throw new IllegalStateException(messageSource.getMessage("category.error.duplicateName", null, locale));
        }
    }
}
