package kkmm.back.board.web.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class NoteForm {

    @NotEmpty
    public String title;

//    TODO 엔터및 빈칸 크기 처리?
    @NotEmpty
    public String contents;
}
