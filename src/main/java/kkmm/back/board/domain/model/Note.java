package kkmm.back.board.domain.model;

import jakarta.persistence.*;
import kkmm.back.board.domain.dto.NoteDto;
import kkmm.back.board.web.dto.NoteForm;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "note", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private int viewCount;

    private LocalDateTime createdAt;

    @ElementCollection(fetch = LAZY)
    private List<UploadFile> files = new ArrayList<>();

    @ElementCollection(fetch = LAZY)
    private List<UploadFile> imageFiles = new ArrayList<>();

    public Note(NoteDto noteDto, Member member, Category category, List<UploadFile> files, List<UploadFile> imageFiles) {
        this.title = noteDto.getTitle();
        this.content = noteDto.getContents();
        this.createdAt = noteDto.getCreatedAt() == null ? LocalDateTime.now() : noteDto.getCreatedAt();
        this.viewCount = 0;
        this.member = member;
        this.category = category;
        this.imageFiles = imageFiles;
        this.files = files;
    }

    public void updateContent(NoteDto noteDto, List<UploadFile> uploadFiles, List<UploadFile> uploadImageFiles) {
        this.title = noteDto.getTitle();
        this.content = noteDto.getContents();

        if (this.files != null ) this.files.addAll(uploadFiles);
        if (this.imageFiles != null ) this.imageFiles.addAll(uploadImageFiles);

        // 일반 파일 삭제 처리
        if (this.files != null && noteDto.getDeleteFiles() != null) {
            this.files.removeIf(file -> noteDto.getDeleteFiles().contains(file.getStoreFileName()));
        }

        // 이미지 파일 삭제 처리
        if (this.imageFiles != null && noteDto.getDeleteImages() != null) {
            this.imageFiles.removeIf(imageFile -> noteDto.getDeleteImages().contains(imageFile.getStoreFileName()));
        }
    }

    // Test Data 처리용
    public Note(Member member, String title, String content, Category category) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now();;
        this.category = category;
        this.viewCount = 0;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


}
