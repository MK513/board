package kkmm.back.board.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import kkmm.back.board.domain.model.Comment;
import kkmm.back.board.web.dto.CommentForm;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentDto {

    public Long id;

    public Long noteId;

    public String noteTitle;

    public Long parentId;

    public int depth;

    public int seq;

    public String contents;

    public String author;

    public LocalDateTime createdAt;

    public CommentDto(CommentForm commentForm) {
        this.id = commentForm.getId();
        this.noteId = commentForm.getNoteId();
        this.noteTitle = commentForm.getNoteTitle();
        this.contents = commentForm.getContents();
        this.author = commentForm.getAuthor();
        this.createdAt = commentForm.getCreatedAt();
        this.depth = commentForm.getDepth();
        this.seq = commentForm.getSeq();
        this.parentId = commentForm.getParentId();
    }
}
