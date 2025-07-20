package kkmm.back.board.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import kkmm.back.board.web.dto.SignupForm;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupDto {

    @Email
    @NotEmpty
    private String email;

    @Pattern(regexp = "^.{8,}$")
    private String password;

    @NotEmpty
    private String name;

    public SignupDto(SignupForm signupForm) {
        this.email = signupForm.getEmail();
        this.password = signupForm.getPassword();
        this.name = signupForm.getName();
    }
}
