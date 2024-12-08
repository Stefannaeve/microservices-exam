package microservices.comment.service;

import lombok.extern.slf4j.Slf4j;
import microservices.comment.models.Comment;
import microservices.comment.repository.CommentRepository;
import microservices.comment.apiResponse.ApiResponse;
import microservices.comment.apiResponse.ApiResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public ApiResponse<List<Comment>> fetchAll() {
        ApiResponseBuilder<List<Comment>> apiResponseBuilder = new ApiResponseBuilder<>();
        try {
            List<Comment> comments = commentRepository.findAll();
            if (comments.isEmpty()) {
                return apiResponseBuilder.failure(HttpStatus.NOT_FOUND, "No comments found");
            }
            return apiResponseBuilder.success(comments);
        } catch (Exception e) {
            log.error("Error fetching all comments: {}", e.getMessage(), e);
            return apiResponseBuilder.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch comments");
        }
    }

    public ApiResponse<Comment> fetchById(Long id) {
        ApiResponseBuilder<Comment> apiResponseBuilder = new ApiResponseBuilder<>();
        try {
            Optional<Comment> comment = commentRepository.findById(id);
            if (comment.isPresent()) {
                return apiResponseBuilder.success(comment.get());
            } else {
                return apiResponseBuilder.failure(HttpStatus.NOT_FOUND, "Comment with id " + id + " not found");
            }
        } catch (Exception e) {
            return apiResponseBuilder.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch comment");
        }
    }

    public ApiResponse<Comment> saveOneComment(Comment comment) {
        ApiResponseBuilder<Comment> apiResponseBuilder = new ApiResponseBuilder<>();
        try {
            Comment savedComment = commentRepository.save(comment);
            return apiResponseBuilder.success(savedComment);
        } catch (Exception e) {
            return apiResponseBuilder.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save comment");
        }
    }
}

