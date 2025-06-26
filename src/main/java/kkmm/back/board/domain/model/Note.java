package kkmm.back.board.domain.model;

import jakarta.persistence.*;
import kkmm.back.board.web.model.NoteForm;
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

    public Note(Member member, String title, String contents) {
        this.member = member;
        this.title = title;
        this.contents = contents;
        this.createdAt = LocalDateTime.now();;
        this.viewCount = 0;
    }

    public Note(NoteForm noteForm, Member member) {
        this.title = noteForm.getTitle();
        this.contents = noteForm.getContents();
        this.createdAt = LocalDateTime.now();
        this.viewCount = 0;
        this.member = member;
    }
}
