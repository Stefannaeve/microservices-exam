package microservices.comment.service;

import microservices.comment.models.Comment;
import microservices.comment.repository.CommentRepository;
import microservices.comment.utils.ApiResponse;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository){
        this.commentRepository = commentRepository;
    }

    public ApiResponse<List<Comment>> fetchAll() {
        return null;
    }
    public Optional<Comment> fetchById(Long id){
        return commentRepository.findById(id);
    }

    public ApiResponse saveOneComment(Comment comment){
        return null;
    }
}
