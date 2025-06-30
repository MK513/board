package kkmm.back.board.domain.init;

import jakarta.annotation.PostConstruct;
import kkmm.back.board.domain.Service.CategoryService;
import kkmm.back.board.domain.model.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryInitialize {

    private final CategoryService categoryService;

    @PostConstruct
    public void init() {
        categoryService.save(new Category("자유게시판"));
    }
}
