package kkmm.back.board.domain.model;

import jakarta.persistence.*;
import kkmm.back.board.web.model.CommentForm;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @Lob
    private String contents;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "note_id")
    private Note note;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Comment(CommentForm commentForm, Member member, Note note) {
        this.contents = commentForm.getContents();
        this.createdAt = LocalDateTime.now();
        this.note = note;
        this.member = member;
    }
}
