package microservices.comment.service;

import microservices.comment.models.Comment;
import microservices.comment.repository.CommentRepository;
import apiResponse.ApiResponse;
import org.springframework.http.HttpStatus;
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
        List<Comment> comments = commentRepository.findAll();
        if (comments.isEmpty()) {
            return new ApiResponse.Failure<>(Optional.empty(), HttpStatus.NOT_FOUND, "No comments found");
        }
        return new ApiResponse.Success<>(Optional.of(comments));
    }

    public Optional<Comment> fetchById(Long id){
        return commentRepository.findById(id);
    }

    public ApiResponse<Comment> saveOneComment(Comment comment){
        try {
            Comment savedComment = commentRepository.save(comment);
            return new ApiResponse.Success<>(Optional.of(savedComment));
        } catch (Exception e) {
            return new ApiResponse.Failure<>(Optional.empty(), HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save comment");
        }
    }
}
