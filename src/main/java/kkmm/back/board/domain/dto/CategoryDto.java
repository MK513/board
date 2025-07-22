package kkmm.back.board.domain.dto;

import jakarta.validation.constraints.Size;
import kkmm.back.board.domain.annotation.NotDefaultCategory;
import kkmm.back.board.web.dto.CategoryForm;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryDto {

    @NotDefaultCategory
    public Long id;

    @Size(max = 10)
    public String name;

    public CategoryDto(String name) {
        this.name = name;
    }

    public CategoryDto(CategoryForm categoryForm) {
        this.id = categoryForm.getId();
        this.name = categoryForm.getName();
    }
}