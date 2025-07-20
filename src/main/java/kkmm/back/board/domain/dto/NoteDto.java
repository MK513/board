package kkmm.back.board.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import kkmm.back.board.domain.model.Note;
import kkmm.back.board.domain.model.UploadFile;
import kkmm.back.board.web.dto.NoteForm;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class NoteDto {

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

    public NoteDto(NoteForm noteForm) {
        this.id = noteForm.getId();
        this.title = noteForm.getTitle();
        this.contents = noteForm.getContents();
        this.author = noteForm.getAuthor();
        this.createdAt = noteForm.getCreatedAt();
        this.categoryId = noteForm.getCategoryId();
        this.categoryName = noteForm.getCategoryName();
        this.viewCount = 0;
        this.commentCount = 0;
    }
}
