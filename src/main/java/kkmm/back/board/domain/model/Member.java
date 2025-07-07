package kkmm.back.board.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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

    private LocalDateTime joinedAt;

    @OneToMany(mappedBy = "member")
    private List<Note> notes = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    public Member(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.joinedAt = LocalDateTime.now();
    }
}
