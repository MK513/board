package kkmm.back.board.web.model;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotEmpty;
import kkmm.back.board.domain.model.Note;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NoteForm {

    public Long id;

    @NotEmpty
    public String title;

    @NotEmpty
    public String contents;

    public int viewCount;

    public String author;

    public NoteForm(Note note) {
        this.id = note.getId();
        this.title = note.getTitle();
        this.contents = note.getContents();
        this.viewCount = note.getViewCount();
        this.author = note.getMember().getName();
    }
}
