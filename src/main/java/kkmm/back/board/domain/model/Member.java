package kkmm.back.board.domain.model;

import jakarta.persistence.*;
import kkmm.back.board.web.model.SignupForm;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String email;

    private String password;

    private String name;

    @OneToMany(mappedBy = "member")
    private List<Note> notes = new ArrayList<>();

    public Member(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public Member(SignupForm signupForm) {
        this.name = signupForm.getName();
        this.email = signupForm.getEmail();
        this.password = signupForm.getPassword();
    }
}
