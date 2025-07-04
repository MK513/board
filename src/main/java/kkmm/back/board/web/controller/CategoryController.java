package kkmm.back.board.web.controller;

import kkmm.back.board.domain.Service.CategoryService;
import kkmm.back.board.domain.model.Category;
import kkmm.back.board.web.model.CategoryForm;
import kkmm.back.board.web.model.CommentForm;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    // TODO categories를 일괄적으로 처리할 수 있는 방법 없을까?

    private final CategoryService categoryService;

    @GetMapping("/manage")
    public String createCategoryForm(Model model) {

        model.addAttribute("categoryForm", new CategoryForm());
        model.addAttribute("categories", categoryService.findAll());

        return "form/manageCategoryForm";
    }

    @PostMapping("/create")
    public String createCategory(@Validated @ModelAttribute CategoryForm categoryForm,
                                 BindingResult bindingResult, Model model) {

        if (categoryService.findByName(categoryForm.getName()) != null) {
            bindingResult.reject("duplicatedError", "이미 존재하는 카테고리입니다.");
        }

        if (!bindingResult.hasErrors()) {
            categoryService.save(new Category(categoryForm));
        }

        model.addAttribute("categories", categoryService.findAll());
        return "form/manageCategoryForm";
    }

    @PostMapping("/remove")
    public String removeCategory(@Validated @ModelAttribute CategoryForm categoryForm,
                                 BindingResult bindingResult, Model model) {

        if (!bindingResult.hasErrors()) {
            categoryService.remove(categoryForm.getId());
        }

        model.addAttribute("categories", categoryService.findAll());
        return "form/manageCategoryForm";
    }
}
