package kkmm.back.board.web.controller;

import kkmm.back.board.ControllerTestSupport;
import kkmm.back.board.domain.dto.NoteDto;
import kkmm.back.board.domain.model.Category;
import kkmm.back.board.domain.model.Member;
import kkmm.back.board.web.SessionConst;
import org.junit.jupiter.api.Test;

import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class NoteControllerTest extends ControllerTestSupport {

    @Test
    void 글쓰기_폼() throws Exception {
        Member member = joinMember("note@test.com", "1", "name");
        createCategory("cat");

        mockMvc.perform(get("/note/write")
                        .sessionAttr(SessionConst.LOGIN_MEMBER, member))
                .andExpect(status().isOk())
                .andExpect(view().name("form/writeNoteForm"))
                .andExpect(model().attributeExists("categories", "note"));
    }

    @Test
    void 게시글_조회() throws Exception {
        Member member = joinMember("note2@test.com", "1", "name");
        Category category = createCategory("cat2");
        NoteDto dto = new NoteDto();
        dto.setTitle("t");
        dto.setContents("c");
        Long id = noteService.save(dto, member, category);

        mockMvc.perform(get("/note/view/{id}", id)
                        .sessionAttr(SessionConst.LOGIN_MEMBER, member))
                .andExpect(status().isOk())
                .andExpect(view().name("form/viewNoteForm"))
                .andExpect(model().attributeExists("note", "comments", "updateComment", "newComment"));
    }

    @Test
    void 게시글_수정() throws Exception {
        Member member = joinMember("edit@test.com", "1", "name");
        Category category = createCategory("cat");
        NoteDto dto = new NoteDto();
        dto.setTitle("title");
        dto.setContents("content");
        Long id = noteService.save(dto, member, category);

        mockMvc.perform(put("/note/edit/{id}", id)
                        .sessionAttr(SessionConst.LOGIN_MEMBER, member)
                        .param("title", "newTitle")
                        .param("contents", "newContent"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/note/view/" + id));

        assertThat(noteService.findById(id).getTitle()).isEqualTo("newTitle");
    }

    @Test
    void 게시글_삭제() throws Exception {
        Member member = joinMember("delete@test.com", "1", "name");
        Category category = createCategory("cat");
        NoteDto dto = new NoteDto();
        dto.setTitle("title");
        dto.setContents("content");
        Long id = noteService.save(dto, member, category);

        mockMvc.perform(delete("/note/delete/{id}", id)
                        .sessionAttr(SessionConst.LOGIN_MEMBER, member))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/board/list"));

        assertThatThrownBy(() -> noteService.findById(id)).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void 게시글_파일_업로드() throws Exception {
        Member member = joinMember("file@test.com", "1", "name");
        Category category = createCategory("cat");

        MockMultipartFile file = new MockMultipartFile("files", "hello.txt", "text/plain", "hello".getBytes());

        mockMvc.perform(multipart("/note/write")
                        .file(file)
                        .param("title", "title")
                        .param("contents", "content")
                        .param("categoryId", category.getId().toString())
                        .sessionAttr(SessionConst.LOGIN_MEMBER, member))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/note/view/*"));

        Long savedId = noteRepository.findAll().get(0).getId();
        assertThat(noteService.findById(savedId).getFiles()).hasSize(1);
        File savedFile = new File("files/" + noteService.findById(savedId).getFiles().get(0).getStoreFileName());
        assertThat(savedFile.exists()).isTrue();
    }
}
