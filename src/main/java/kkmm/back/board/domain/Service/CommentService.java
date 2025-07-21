package kkmm.back.board.domain.Service;

import jakarta.validation.constraints.NotEmpty;
import kkmm.back.board.domain.dto.CommentDto;
import kkmm.back.board.domain.model.Comment;
import kkmm.back.board.domain.model.Member;
import kkmm.back.board.domain.model.Note;
import kkmm.back.board.domain.repository.CommentRepository;
import kkmm.back.board.domain.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final NoteRepository noteRepository;

    @Transactional
    public Long save(CommentDto commentDto, Member member, Long noteId) {
        Note note = noteRepository.findById(noteId).orElseThrow();
        Comment parentComment = null;
        if (commentDto.getParentId() != null) {
            parentComment = commentRepository.findById(commentDto.getParentId()).orElse(null);
        }

        Comment comment = new Comment(commentDto, member, note, parentComment);
        commentRepository.save(comment);

        if (parentComment != null) {
            parentComment.getChildren().add(comment);
        }

        return comment.getId();
    }

    @Transactional
    public void update(Long id, @NotEmpty String contents) {
        Comment comment = commentRepository.findById(id).orElseThrow();
        comment.updateContents(contents);
    }

    public List<Comment> findComments(Long id) {
        return commentRepository.findAllByNoteId(id);
    }

    public Comment findById(Long id) {
        return commentRepository.findById(id).orElseThrow();
    }

    @Transactional
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
