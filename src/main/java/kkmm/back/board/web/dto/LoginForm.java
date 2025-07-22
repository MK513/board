package kkmm.back.board.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginForm {

    @Email
    @NotEmpty
    public String email;

    @NotEmpty
    @Pattern(regexp = "^.{8,}$")
    public String password;
}
