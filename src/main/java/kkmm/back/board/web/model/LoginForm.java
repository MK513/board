package kkmm.back.board.web.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginForm {

    @Email
    @NotEmpty
    public String email;

    @NotEmpty
    public String password;
}
