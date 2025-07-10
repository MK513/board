package kkmm.back.board.web.dto;

import jakarta.validation.constraints.NotEmpty;
import kkmm.back.board.domain.model.Note;
import kkmm.back.board.domain.model.UploadFile;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

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

    private List<MultipartFile> files;
    private List<MultipartFile> images;

    private List<UploadFile> attachFiles;
    private List<UploadFile> attachImages;

    private List<String> deleteFiles;
    private List<String> deleteImages;

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

        this.attachFiles = note.getFiles();
        this.attachImages = note.getImageFiles();

        this.deleteFiles = null;
        this.deleteImages = null;
    }
}
