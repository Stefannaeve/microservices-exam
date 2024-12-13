
package microservices.comment.controller;

import microservices.comment.apiResponse.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import microservices.comment.apiResponse.ApiResponseBuilder;
import microservices.comment.models.Comment;
import microservices.comment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/fetchAll")
    public ResponseEntity<ApiResponse<List<Comment>>> fetchAll() {
        ApiResponse<List<Comment>> apiResponse = commentService.fetchAll();

        switch (apiResponse) {
            case ApiResponse.Success<List<Comment>> success -> {
                if (success.value().isPresent()) {
                    log.info("Fetched all comments successfully. Total comments: {}", success.value().get().size());
                } else {
                    log.info("Fetched all comments successfully, but no comments found.");
                }
                return ResponseEntity.status(HttpStatus.OK).body(success);
            }
            case ApiResponse.Failure<List<Comment>> failure -> {
                log.error("Failed to fetch all comments. Error: {}", failure.errorMessage());
                return new ResponseEntity<>(failure, failure.status());
            }
        }
    }

    @GetMapping("/fetchCommentById/{id}")
    public ResponseEntity<ApiResponse<Comment>> fetchCommentById(@PathVariable long id) {
        ApiResponse<Comment> apiResponse = commentService.fetchById(id);

        switch (apiResponse) {
            case ApiResponse.Success<Comment> success -> {
                if (success.value().isPresent()) {
                    log.info("Fetched comment with id: {}", success.value().get().getId());
                } else {
                    log.info("Fetched comment successfully, but no comment data found.");
                }
                return ResponseEntity.status(HttpStatus.OK).body(success);
            }
            case ApiResponse.Failure<Comment> failure -> {
                log.error("Failed to fetch comment with id: {}. Error: {}", id, failure.errorMessage());
                return new ResponseEntity<>(failure, failure.status());
            }
        }
    }

    @PostMapping("/saveOneComment")
    public ResponseEntity<ApiResponse<Comment>> saveOneComment(@RequestBody Comment comment) {
        ApiResponse<Comment> apiResponse = commentService.saveOneComment(comment);

        switch (apiResponse) {
            case ApiResponse.Success<Comment> success -> {
                if (success.value().isPresent()) {
                    log.info("Saved comment with id: {}", success.value().get().getId());
                } else {
                    log.info("Comment saved successfully.");
                }
                return ResponseEntity.status(HttpStatus.CREATED).body(success);
            }
            case ApiResponse.Failure<Comment> failure -> {
                log.error("Failed to save comment. Error: {}", failure.errorMessage());
                return new ResponseEntity<>(failure, failure.status());
            }
        }
    }

    @GetMapping("/user/{userId}/book/{bookId}")
    public ResponseEntity<ApiResponse<List<Comment>>> fetchCommentsByUserAndBook(@PathVariable Long userId, @PathVariable Long bookId) {
        ApiResponse<List<Comment>> apiResponse = commentService.fetchCommentsByUserAndBook(userId, bookId);

        switch (apiResponse) {
            case ApiResponse.Success<List<Comment>> success -> {
                if (success.value().isPresent()) {
                    log.info("Fetched comments for userId: {} and bookId: {}. Total comments: {}", userId, bookId, success.value().get().size());
                } else {
                    log.info("Fetched comments for userId: {} and bookId: {}, but no comments found.", userId, bookId);
                }
                return ResponseEntity.status(HttpStatus.OK).body(success);
            }
            case ApiResponse.Failure<List<Comment>> failure -> {
                log.error("Failed to fetch comments for userId: {} and bookId: {}. Error: {}", userId, bookId, failure.errorMessage());
                return new ResponseEntity<>(failure, failure.status());
            }
        }
    }


    @PutMapping("/updateComment/user/{userId}/book/{bookId}/comment/{commentId}")
    public ResponseEntity<ApiResponse<Comment>> updateComment(@PathVariable Long userId, @PathVariable Long bookId, @PathVariable Long commentId, @RequestBody Comment updateComment) {
        ApiResponse<Comment> apiResponse = commentService.updateComment(userId, bookId, commentId, updateComment);
  
        switch (apiResponse) {
            case ApiResponse.Success<Comment> success -> {
                if (success.value().isPresent()) {
                    log.info("Updated comment with id: {}", success.value().get().getId());
                } else {
                    log.info("Comment updated successfully.");
                }
                return ResponseEntity.status(HttpStatus.OK).body(success);
            }
            case ApiResponse.Failure<Comment> failure -> {
                log.error("Failed to update comment with commentId: {}. Error: {}", commentId, failure.errorMessage());
                return new ResponseEntity<>(failure, failure.status());
            }
        }
    }

    @DeleteMapping("/delete/user/{userId}/book/{bookId}/comment/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable Long userId, @PathVariable Long bookId, @PathVariable Long commentId) {
        ApiResponse<Void> apiResponse = commentService.deleteCommentById(userId, bookId, commentId);

        switch (apiResponse) {
            case ApiResponse.Success<Void> success -> {
                log.info("Successfully deleted comment with id: {}", commentId);
                return ResponseEntity.status(HttpStatus.OK).body(success);
            }
            case ApiResponse.Failure<Void> failure -> {
                log.error("Failed to delete comment with id: {}. Error: {}", commentId, failure.errorMessage());
                return new ResponseEntity<>(failure, failure.status());
            }
        }
    }
}
