
package microservices.comment.controller;

import lombok.extern.slf4j.Slf4j;
import microservices.comment.models.Comment;
import microservices.comment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<Optional<List<Comment>>> fetchAll() {
        return commentService.fetchAll();
    }

    @GetMapping("/fetchCommentById/{id}")
    public ResponseEntity<Optional<Comment>> fetchCommentById(@PathVariable long id) {
        return commentService.fetchById(id);
    }

    @PostMapping("/saveOneComment")
    public ResponseEntity<Optional<Comment>> saveOneComment(@RequestBody Comment comment) {
        return commentService.saveOneComment(comment);
    }

    @GetMapping("/user/{userId}/book/{bookId}")
    public ResponseEntity<Optional<List<Comment>>> fetchCommentsByUserAndBook(@PathVariable Long userId, @PathVariable Long bookId) {
        return commentService.fetchCommentsByUserAndBook(userId, bookId);
    }


    @PutMapping("/updateComment/user/{userId}/book/{bookId}/comment/{commentId}")
    public ResponseEntity<Optional<Comment>> updateComment(@PathVariable Long userId, @PathVariable Long bookId, @PathVariable Long commentId, @RequestBody Comment updateComment) {
        return commentService.updateComment(userId, bookId, commentId, updateComment);
    }

    @DeleteMapping("/delete/user/{userId}/book/{bookId}/comment/{commentId}")
    public ResponseEntity<Optional<Comment>> deleteComment(@PathVariable Long userId, @PathVariable Long bookId, @PathVariable Long commentId) {
        return commentService.deleteCommentById(userId, bookId, commentId);
    }
}
