package kkmm.back.board.web.dto;

import jakarta.validation.constraints.NotEmpty;
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
