package kkmm.back.board.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import kkmm.back.board.domain.dto.CommentDto;
import kkmm.back.board.web.dto.CommentForm;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String contents;

    private LocalDateTime createdAt;

    private int depth;

    private int seq;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "note_id")
    private Note note;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Comment> children = new ArrayList<>();

    public Comment(CommentDto commentDto, Member member, Note note, Comment parentComment) {
        this.contents = commentDto.getContents();
        this.createdAt = LocalDateTime.now();
        this.note = note;
        this.member = member;
        this.parent = parentComment;
        this.depth = parentComment == null ? 0 : parentComment.getDepth() + 1;
        this.seq = parentComment == null ? note.getComments().size() : parentComment.getSeq();
    }

    public void updateContents(@NotEmpty String contents) {
        this.contents = contents;
    }
}
