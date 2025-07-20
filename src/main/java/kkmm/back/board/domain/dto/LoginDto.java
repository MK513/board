package kkmm.back.board.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginDto {

    //@Email
    @NotEmpty
    public String email;

    @NotEmpty
    public String password;
}
