package kkmm.back.board.domain.model;

import jakarta.persistence.*;
import kkmm.back.board.domain.dto.CategoryDto;
import kkmm.back.board.web.dto.CategoryForm;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    private int count;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Note> notes = new ArrayList<>();

    public Category(String name) {
        this.name = name;
        this.count = 0;
    }

    public Category(CategoryDto categoryDto) {
        this.name = categoryDto.getName();
        this.count = 0;
    }

    public void increaseCount() {
        count++;
    }

    public void decreaseCount() { count--; }
}
