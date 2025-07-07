package kkmm.back.board.web.dto;

import jakarta.validation.constraints.NotEmpty;
import kkmm.back.board.domain.model.Comment;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentForm {

    public Long id;

    public Long parentId;

    public int depth;

    public int seq;

    @NotEmpty
    public String contents;

    public String author;

    public LocalDateTime createdAt;

    public CommentForm(Comment comment) {
        this.id = comment.getId();
        this.contents = comment.getContents();
        this.author = comment.getMember().getName();
        this.createdAt = comment.getCreatedAt();
        this.depth = comment.getDepth();
        this.seq = comment.getSeq();
        this.parentId = comment.getParent() == null ? null : comment.getParent().getId();
    }

    public CommentForm(String author) {
        this.author = author;
    }
}
