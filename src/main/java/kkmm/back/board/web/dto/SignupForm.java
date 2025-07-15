package kkmm.back.board.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SignupForm {

    @Email
    @NotEmpty
    private String email;

    @Pattern(regexp = "^.{8,}$")
    private String password;

    @NotEmpty
    private String name;
}
