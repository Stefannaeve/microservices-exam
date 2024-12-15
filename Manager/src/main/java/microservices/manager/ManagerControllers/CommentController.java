package microservices.manager.ManagerControllers;

import lombok.extern.slf4j.Slf4j;
import microservices.manager.ManagerClients.CommentClient;
import microservices.manager.ManagerServices.CommentService;
import microservices.manager.dtos.CommentDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/manager/comment")
public class CommentController {
    CommentClient commentClient;
    CommentService commentService;

    public CommentController(CommentClient commentClient, CommentService commentService){
        this.commentClient = commentClient;
        this.commentService = commentService;
    }

    @GetMapping("/fetchAll")
    public ResponseEntity<Optional<List<CommentDTO>>> fetchAllComments(){
        return commentClient.externalGetAllComments();
    }

    @GetMapping("fetchById/{id}")
    public ResponseEntity<Optional<CommentDTO>> fetchCommentById(@PathVariable long id){
        return commentClient.fetchById(id);
    }

    @PostMapping("/saveOneComment")
    public ResponseEntity<Optional<CommentDTO>> saveOneComment(@RequestBody CommentDTO commentDTO){
        return commentService.saveById(commentDTO);
    }

    @GetMapping("fetchCommentsByUserAndBook/user/{userId}/book/{bookId}")
    public ResponseEntity<Optional<List<CommentDTO>>> fetchCommentsByUserAndBook(@PathVariable Long userId, @PathVariable Long bookId){
        log.info("userId: {}, bookId: {}", userId, bookId);
        return commentService.fetchByUserIdAndBookId(userId, bookId);
    }
}