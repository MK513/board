package kkmm.back.board.web.model;

import jakarta.validation.constraints.NotEmpty;
import kkmm.back.board.domain.model.Note;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class NoteForm {

    public Long id;

    @NotEmpty
    public String title;

    @NotEmpty
    public String contents;

    public int commentCount;

    public int viewCount;

    public String author;

    public Long categoryId;

    public String categoryName;

    private LocalDateTime createdAt;

    public NoteForm(Note note) {
        this.id = note.getId();
        this.title = note.getTitle();
        this.contents = note.getContent();
        this.viewCount = note.getViewCount();
        this.author = note.getMember().getName();
        this.createdAt = note.getCreatedAt();
        this.commentCount = note.getComments().size();
        this.categoryId = note.getCategory().getId();
        this.categoryName = note.getCategory().getName();
    }
}
