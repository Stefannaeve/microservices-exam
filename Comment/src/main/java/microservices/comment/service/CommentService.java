
package microservices.comment.service;

import lombok.extern.slf4j.Slf4j;
import microservices.comment.apiResponse.ApiResponse;
import microservices.comment.apiResponse.ApiResponseBuilder;
import microservices.comment.apiResponse.ResponseEntityInitializer;
import microservices.comment.eventDriven.CommentEventPublisher;
import microservices.comment.models.Comment;
import microservices.comment.repository.CommentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
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

    public ResponseEntity<Optional<List<Comment>>> fetchAll() {
        Optional<List<Comment>> comments = Optional.empty();
        try {
            comments = Optional.of(commentRepository.findAll());
            if (comments.isEmpty()) {
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        false,
                        "No comments found",
                        HttpStatus.NOT_FOUND,
                        comments
                );
            }
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.OK,
                    comments
            );
        } catch (Exception e) {
            log.error("Error fetching all comments: {}", e.getMessage(), e);
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    false,
                    "Something went wrong...",
                    HttpStatus.INTERNAL_SERVER_ERROR, comments
            );
        }
    }

    public ResponseEntity<Optional<Comment>> fetchById(Long id) {
        Optional<Comment> comment = Optional.empty();
        try {
            comment = commentRepository.findById(id);
            if (comment.isPresent()) {
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        comment
                );
            } else {
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        false,
                        "Comment not found",
                        HttpStatus.NOT_FOUND,
                        comment
                );
            }
        } catch (Exception e) {
            log.error("Error fetching comment: {}", e.getMessage());
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    false,
                    "Something went wrong...",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    comment
            );
        }
    }

    public ResponseEntity<Optional<Comment>> saveOneComment(Comment comment) {
        Optional<Comment> savedComment = Optional.empty();
        try {
            if (comment.getUserId() == null || comment.getUserId() <= 0) {
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        false,
                        "Invalid userID",
                        HttpStatus.BAD_REQUEST,
                        savedComment
                );
            }
            savedComment = Optional.of(commentRepository.save(comment));
            commentEventPublisher.publishCommentCreatedEvent(
                    savedComment
                            .get()
                            .getId(),
                    savedComment
                            .get()
                            .getUserId(),
                    savedComment
                            .get().
                            getBookId());
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.CREATED,
                    savedComment
            );
        } catch (Exception e) {
            log.error("Error saving comment: {}", e.getMessage(), e);
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    false,
                    "Something went wrong...",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    savedComment
            );
        }
    }

    public ResponseEntity<Optional<List<Comment>>> fetchCommentsByUserAndBook(Long userId, Long bookId) {
        Optional<List<Comment>> comments = Optional.empty();
        try {
            comments = Optional.ofNullable(commentRepository.findByUserIdAndBookId(userId, bookId));
            if (comments.isEmpty()) {
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        false,
                        "Found no comments for the specified user and book",
                        HttpStatus.NOT_FOUND,
                        comments
                );
            }
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.OK,
                    comments
            );
        } catch (Exception e) {
            log.error("Error fetching comments: {}", e.getMessage());
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    false,
                    "Something went wrong...",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    comments
            );
        }
    }

    public ResponseEntity<Optional<Comment>> updateComment(Long userId, Long bookId, Long commentId, Comment updatedComment) {
        Optional<Comment> findComment = Optional.empty();
        Optional<Comment> comment = Optional.empty();
        Optional<Comment> savedComment = Optional.empty();
        try {
            findComment = commentRepository.findByIdAndUserIdAndBookId(commentId, userId, bookId);
            if (findComment.isEmpty()) {
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        false,
                        "Unable to fetch",
                        HttpStatus.NOT_FOUND,
                        findComment
                );
            }
            comment = Optional.of(findComment.get());
            comment.get().setText(updatedComment.getText());
            savedComment = Optional.of(commentRepository.save(comment.get()));
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.OK,
                    savedComment
            );
        } catch (Exception e) {
            log.error("Error updating comment with id {}: {}", commentId, e.getMessage());
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    false,
                    "Something went wrong...",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    savedComment
            );
        }
    }

    public ResponseEntity<Optional<Comment>> deleteCommentById(Long userId, Long bookId, Long commentId) {
        Optional<Comment> comment = Optional.empty();
        try {
            comment = commentRepository.findByIdAndUserIdAndBookId(commentId, userId, bookId);
            if (comment.isPresent()) {
                commentRepository.delete(comment.get());
                log.info("Deleted comment with id: {}", commentId);
                commentEventPublisher.publishCommentDeletedEvent(commentId);
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        comment
                );
            } else {
                log.warn("Comment with id {} not found for userId {} and bookId {}", commentId, userId, bookId);
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        false,
                        "Comment not found for the specific user and book",
                        HttpStatus.NOT_FOUND,
                        comment
                );
            }
        } catch (Exception e) {
            log.error("Error deleting comment with id {}: {}", commentId, e.getMessage());
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    false,
                    "Something went wrong...",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    comment);
        }
    }
}