package kkmm.back.board.domain.Service;

import jakarta.transaction.Transactional;
import kkmm.back.board.domain.dto.CommentDto;
import kkmm.back.board.domain.model.Category;
import kkmm.back.board.domain.model.Comment;
import kkmm.back.board.domain.model.Member;
import kkmm.back.board.domain.model.Note;
import kkmm.back.board.domain.repository.CategoryRepository;
import kkmm.back.board.domain.repository.MemberRepository;
import kkmm.back.board.domain.repository.NoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
@Slf4j
class CommentServiceTest {

    @Autowired
    CommentService commentService;

    @Autowired
    NoteRepository noteRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    public void 댓글_저장() throws Exception {
        //given
        Member member = memberRepository.save(new Member("a", "a", "a"));
        Category category = categoryRepository.save(new Category("dummy"));
        Note note = noteRepository.save(new Note(member, "a", "a", category));

        CommentDto commentDto = new CommentDto();
        commentDto.setNoteId(note.getId());
        commentDto.setContents("contents");

        //when
        Long savedId = commentService.save(commentDto, member);
        Comment findComment = commentService.findById(savedId);

        //then

        assertThat(findComment.getNote()).isEqualTo(note);
        assertThat(findComment.getMember()).isEqualTo(member);
        assertThat(findComment.getNote().getCategory()).isEqualTo(category);
        assertThat(findComment.getContents()).isEqualTo(commentDto.getContents());
    }
    
    @Test
    public void 대댓글_저장() throws Exception {
        //given
        Member member = memberRepository.save(new Member("a", "a", "a"));
        Category category = categoryRepository.save(new Category("dummy"));
        Note note = noteRepository.save(new Note(member, "a", "a", category));

        CommentDto parentDto = new CommentDto();
        parentDto.setNoteId(note.getId());
        parentDto.setContents("contents");

        Long parentdId = commentService.save(parentDto, member);
        Comment parentComment = commentService.findById(parentdId);

        //when
        CommentDto childDto = new CommentDto();
        childDto.setParentId(parentComment.getId());
        childDto.setNoteId(note.getId());
        childDto.setContents("contents");

        Long childId = commentService.save(childDto, member);
        Comment childComment = commentService.findById(childId);

        //then
        assertThat(parentComment.getChildren().get(0)).isEqualTo(childComment);
        assertThat(childComment.getParent()).isEqualTo(parentComment);
    }

    @Test
    public void 댓글_수정() throws Exception {
        //given
        Member member = memberRepository.save(new Member("a", "a", "a"));
        Category category = categoryRepository.save(new Category("dummy"));
        Note note = noteRepository.save(new Note(member, "a", "a", category));

        CommentDto commentDto = new CommentDto();
        commentDto.setNoteId(note.getId());
        commentDto.setContents("contents");

        Long savedId = commentService.save(commentDto, member);
        Comment savedComment = commentService.findById(savedId);

        //when
        commentService.update(savedComment.getId(), "new contents");

        //then
        assertThat(savedComment.getContents()).isEqualTo("new contents");
    }
}