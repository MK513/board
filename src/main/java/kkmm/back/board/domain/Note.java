package kkmm.back.board.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Note {

    @Id @GeneratedValue
    @Column(name = "note_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    private String title;

    private String contents;

    private LocalDateTime createdAt;

    private int viewCount;

    public Note(NoteDTO noteDTO, Member member) {
        this.title = noteDTO.getTitle();
        this.contents = noteDTO.getContents();
        this.createdAt = LocalDateTime.now();
        this.viewCount = 0;
        this.member = member;
    }
}
