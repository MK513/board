package kkmm.back.board.web.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginForm {

    //@Email
    @NotEmpty
    public String email;

    @NotEmpty
    public String password;
}
