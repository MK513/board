package kkmm.back.board.domain;

import jakarta.persistence.*;
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

    public Member(MemberDTO memberDTO) {
        this.name = memberDTO.getName();
        this.email = memberDTO.getEmail();
        this.password = memberDTO.getPassword();
    }
}
