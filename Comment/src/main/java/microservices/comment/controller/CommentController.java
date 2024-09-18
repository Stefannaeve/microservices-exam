package microservices.comment.controller;

import apiResponse.ApiResponse;
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

    CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    //    @GetMapping("/fetchAll")
//    public ResponseEntity<ApiResponse<List<Comment>>> fetchAll() {
//        ApiResponse<List<Comment>> comments = commentService.fetchAll();
//
//        switch (comments) {
//            case ApiResponse.Success<List<Comment>> success -> {
//                return ResponseEntity.status(HttpStatus.OK).body(comments);
//            }
//            case ApiResponse.Failure<List<Comment>> failure -> {
//                log.error(failure.errorMessage());
//                return ResponseEntity.status(failure.status()).body(comments);
//            }
//        }
//    }
    @GetMapping("/fetchAll")
    public ResponseEntity<ApiResponse<List<Comment>>> fetchAll() {
        ApiResponse<List<Comment>> comments = commentService.fetchAll();

        switch (comments) {
            case ApiResponse.Success<List<Comment>> success -> {
                return ResponseEntity.status(HttpStatus.OK).body(success);
            }
            case ApiResponse.Failure<List<Comment>> failure -> {
                log.error(failure.errorMessage());
                return ResponseEntity.status(failure.status()).body(failure);
            }
        }
    }

    @GetMapping("/fetchCommentById/{id}")
    public ResponseEntity<ApiResponse<Comment>> fetchCommentById(@PathVariable long id) {
        ApiResponse<Comment> commentResponse = commentService.fetchById(id);

        switch (commentResponse) {
            case ApiResponse.Success<Comment> success -> {
                return ResponseEntity.status(HttpStatus.OK).body(commentResponse);
            }
            case ApiResponse.Failure<Comment> failure -> {
                log.error(failure.errorMessage());
                return ResponseEntity.status(failure.status()).body(commentResponse);
            }
        }
    }

    /*
    @GetMapping("/fetchAll")
    public ResponseEntity<List<Book>> fetchAll() {
        List<Book> books = bookService.fetchAll();

        return ResponseEntity.status(HttpStatus.OK).body(books);
    }
     */

    @PostMapping("/saveOneComment")
    public ResponseEntity<ApiResponse<Comment>> saveOneComment(@RequestBody Comment comment) {
        ApiResponse<Comment> savedComment = commentService.saveOneComment(comment);

        switch (savedComment) {
            case ApiResponse.Success<Comment> success -> {
                return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
            }
            case ApiResponse.Failure<Comment> failure -> {
                log.error(failure.errorMessage());
                return ResponseEntity.status(failure.status()).body(savedComment);
            }
        }
    }

}
