package kkmm.back.board.domain.model;

import jakarta.persistence.*;
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
@Data
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

    private String content;

    private LocalDateTime createdAt;

    private int viewCount;

    public Note(NoteForm noteForm, Member member, Category category) {
        this.title = noteForm.getTitle();
        this.content = noteForm.getContents();
        this.createdAt = noteForm.getCreatedAt() == null ? LocalDateTime.now() : noteForm.getCreatedAt();
        this.viewCount = 0;
        this.member = member;
        this.category = category;
    }

    public void updateContent(String title, String content) {
        this.content = content;
        this.title = title;
    }

    // Test Data 처리용

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Note(Member member, String title, String content, Category category) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now();;
        this.category = category;
        this.viewCount = 0;
    }


}
