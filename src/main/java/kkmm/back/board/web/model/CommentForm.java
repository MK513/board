package kkmm.back.board.web.model;

import jakarta.validation.constraints.NotEmpty;
import kkmm.back.board.domain.model.Comment;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentForm {

    @NotEmpty
    public String contents;

    public String author;

    public LocalDateTime createdAt;

    public CommentForm(Comment comment) {
        this.contents = comment.getContents();
        this.author = comment.getMember().getName();
        this.createdAt = comment.getCreatedAt();
    }

    public CommentForm(String author) {
        this.author = author;
    }
}
