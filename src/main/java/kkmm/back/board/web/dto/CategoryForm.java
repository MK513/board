package kkmm.back.board.web.dto;

import jakarta.validation.constraints.Size;
import kkmm.back.board.domain.annotation.NotDefaultCategory;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryForm {

    @NotDefaultCategory
    public Long id;

    @Size(max = 10)
    public String name;
}