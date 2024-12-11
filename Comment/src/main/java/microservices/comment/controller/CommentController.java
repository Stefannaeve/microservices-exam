package microservices.comment.controller;

import microservices.comment.apiResponse.ApiResponse;
import lombok.extern.slf4j.Slf4j;
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
        ApiResponse<List<Comment>> commentResponse = commentService.fetchAll();

        switch (commentResponse) {
            case ApiResponse.Success<List<Comment>> success -> {
                return ResponseEntity.status(HttpStatus.OK).body(success);
            }
            case ApiResponse.Failure<List<Comment>> failure -> {
                return new ResponseEntity<>(failure, failure.status());
            }
        }
    }

    @GetMapping("/fetchCommentById/{id}")
    public ResponseEntity<ApiResponse<Comment>> fetchCommentById(@PathVariable long id) {
        ApiResponse<Comment> commentResponse = commentService.fetchById(id);

        switch (commentResponse) {
            case ApiResponse.Success<Comment> success -> {
                return ResponseEntity.status(HttpStatus.OK).body(success);
            }
            case ApiResponse.Failure<Comment> failure -> {
                return new ResponseEntity<>(failure, failure.status());
            }
        }
    }

    @GetMapping("/user/{userId}/book/{bookId}")
    public ResponseEntity<ApiResponse<List<Comment>>> fetchCommentsByUserAndBook(@PathVariable Long userId, @PathVariable Long bookId) {
        ApiResponse<List<Comment>> commentResponse = commentService.fetchCommentsByUserAndBook(userId, bookId);

        switch (commentResponse) {
            case ApiResponse.Success<List<Comment>> success -> {
                return ResponseEntity.status(HttpStatus.OK).body(success);
            }
            case ApiResponse.Failure<List<Comment>> failure -> {
                return new ResponseEntity<>(failure, failure.status());
            }
        }
    }
}
