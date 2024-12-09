package microservices.comment.service;

import lombok.extern.slf4j.Slf4j;
import microservices.comment.apiResponse.ApiResponse;
import microservices.comment.apiResponse.ApiResponseBuilder;
import microservices.comment.eventDriven.CommentEventPublisher;
import microservices.comment.models.Comment;
import microservices.comment.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentEventPublisher commentEventPublisher;

    public CommentService(CommentRepository commentRepository, CommentEventPublisher commentEventPublisher) {
        this.commentRepository = commentRepository;
        this.commentEventPublisher = commentEventPublisher;
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
            if (comment.getUserId() == null || comment.getUserId() <= 0) {
                return apiResponseBuilder.failure(HttpStatus.BAD_REQUEST, "Invalid userId");
            }
            if (comment.getBookId() == null || comment.getBookId() <= 0) {
                return apiResponseBuilder.failure(HttpStatus.BAD_REQUEST, "Invalid bookId");
            }
            Comment savedComment = commentRepository.save(comment);
            commentEventPublisher.publishCommentCreatedEvent(savedComment);
            return apiResponseBuilder.success(savedComment, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error saving comment: {}", e.getMessage(), e);
            return apiResponseBuilder.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save comment");
        }
    }

    public ApiResponse<Comment> deleteCommentById(Long id) {
        ApiResponseBuilder<Comment> apiResponseBuilder = new ApiResponseBuilder<>();
        try {
            Optional<Comment> comment = commentRepository.findById(id);
            if (comment.isPresent()) {
                commentRepository.delete(comment.get());
                return apiResponseBuilder.success();
            } else {
                return apiResponseBuilder.failure(HttpStatus.NOT_FOUND, "Comment with id " + id + " not found");
            }
        } catch (Exception e) {
            return apiResponseBuilder.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete comment");
        }
    }

    public ApiResponse<List<Comment>> fetchCommentsByUserAndBook(Long userId, Long bookId) {
        ApiResponseBuilder<List<Comment>> apiResponseBuilder = new ApiResponseBuilder<>();
        try {
            List<Comment> comment = commentRepository.findByUserIdAndBookId(userId, bookId);
            if (comment.isEmpty()) {
                return apiResponseBuilder.failure(HttpStatus.NOT_FOUND, "No comments found for the specified user and book");
            }
            return apiResponseBuilder.success(comment);
        } catch (Exception e) {
            return apiResponseBuilder.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch comments");
        }
    }
}
