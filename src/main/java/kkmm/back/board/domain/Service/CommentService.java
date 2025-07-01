package kkmm.back.board.domain.Service;

import kkmm.back.board.domain.model.Comment;
import kkmm.back.board.domain.repository.CommentRepository;
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

    public Comment findOne(Long id) {
        return commentRepository.findOne(id);
    }

    public List<Comment> findComments(Long id) {
        return commentRepository.findCommentsByNoteId(id);
    }

}
