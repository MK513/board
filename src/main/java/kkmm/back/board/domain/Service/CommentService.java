package kkmm.back.board.domain.Service;

import jakarta.validation.constraints.NotEmpty;
import kkmm.back.board.domain.model.Comment;
import kkmm.back.board.domain.repository.CommentRepository;
import kkmm.back.board.domain.repository.jpa.CommentRepositoryRegacy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    public Long save(Comment comment) {
        commentRepository.save(comment);
        return comment.getId();
    }

    @Transactional
    public void updateComment(Long id, @NotEmpty String contents) {
        Comment comment = commentRepository.findById(id).orElseThrow();
        comment.updateContents(contents);
    }

    public List<Comment> findComments(Long id) {
        return commentRepository.findAllByNoteId(id);
    }

    public Comment findOne(Long id) {
        return commentRepository.findById(id).orElseThrow();
    }

    @Transactional
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
