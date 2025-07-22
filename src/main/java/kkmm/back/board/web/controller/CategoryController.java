package kkmm.back.board.web.controller;

import kkmm.back.board.domain.Service.CategoryService;
import kkmm.back.board.domain.dto.CategoryDto;
import kkmm.back.board.domain.model.Category;
import kkmm.back.board.web.dto.CategoryForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final MessageSource messageSource;

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
            bindingResult.reject("duplicatedError",
                    messageSource.getMessage("category.error.duplicateName", null, null));
        }

        if (!bindingResult.hasErrors()) {
            categoryService.save(new CategoryDto(categoryForm));
        }

        model.addAttribute("categories", categoryService.findAll());
        return "form/manageCategoryForm";
    }

    @DeleteMapping("/remove")
    public String removeCategory(@Validated @ModelAttribute CategoryForm categoryForm,
                                 BindingResult bindingResult, Model model) {

        if (!bindingResult.hasErrors()) {
            categoryService.remove(categoryForm.getId());
        }

        model.addAttribute("categories", categoryService.findAll());
        return "form/manageCategoryForm";
    }
}
