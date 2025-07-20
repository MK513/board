package kkmm.back.board.domain.dto;

import jakarta.validation.constraints.Size;
import kkmm.back.board.domain.annotation.NotDefaultCategory;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryDto {

    @NotDefaultCategory
    public Long id;

    @Size(max = 10)
    public String name;
}