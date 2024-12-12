
package microservices.comment.service;

import lombok.extern.slf4j.Slf4j;
import microservices.comment.apiResponse.ApiResponse;
import microservices.comment.apiResponse.ApiResponseBuilder;
import microservices.comment.eventDriven.CommentEventPublisher;
import microservices.comment.models.Comment;
import microservices.comment.repository.CommentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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
            if (comment.getUserId() == null || comment.getUserId() <= 0) {
                return apiResponseBuilder.failure(HttpStatus.BAD_REQUEST, "Invalid userId");
            }
            Comment savedComment = commentRepository.save(comment);
            commentEventPublisher.publishCommentCreatedEvent(savedComment.getId(), savedComment.getUserId(), savedComment.getBookId());
            return apiResponseBuilder.success(savedComment, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error saving comment: {}", e.getMessage(), e);
            return apiResponseBuilder.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save comment");
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

    public ApiResponse<Comment> updateComment(Long userId, Long bookId, Long commentId, Comment updatedComment) {
        ApiResponseBuilder<Comment> apiResponseBuilder = new ApiResponseBuilder<>();
        try {
            Optional<Comment> findComment = commentRepository.findByIdAndUserIdAndBookId(commentId, userId, bookId);
            if (findComment.isEmpty()) {
                return apiResponseBuilder.failure(HttpStatus.NOT_FOUND, "Comment not found for the specified user and book");
            }
            Comment comment = findComment.get();
            comment.setText(updatedComment.getText());
            Comment savedComment = commentRepository.save(comment);
            return apiResponseBuilder.success(savedComment);
        } catch (Exception e) {
            log.error("Error updating comment with id {}: {}", commentId, e.getMessage());
            return apiResponseBuilder.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update comment");
        }
    }

    public ApiResponse<Void> deleteCommentById(Long userId, Long bookId, Long commentId) {
        ApiResponseBuilder<Void> apiResponseBuilder = new ApiResponseBuilder<>();
        try {
            Optional<Comment> comment = commentRepository.findByIdAndUserIdAndBookId(commentId, userId, bookId);
            if (comment.isPresent()) {
                commentRepository.delete(comment.get());
                log.info("Deleted comment with id: {}", commentId);
                commentEventPublisher.publishCommentDeletedEvent(commentId);
                return apiResponseBuilder.success();
            } else {
                log.warn("Comment with id {} not found for userId {} and bookId {}", commentId, userId, bookId);
                return apiResponseBuilder.failure(HttpStatus.NOT_FOUND, "Comment not found for the specified user and book");
            }
        } catch (Exception e) {
            log.error("Error deleting comment with id {}: {}", commentId, e.getMessage());
            return apiResponseBuilder.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete comment");
        }
    }

}