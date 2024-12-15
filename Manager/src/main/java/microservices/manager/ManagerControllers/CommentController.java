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

    //region GET
    @GetMapping("/fetchAll")
    public ResponseEntity<Optional<List<CommentDTO>>> fetchAllComments(){
        return commentClient.externalFetchAllComments();
    }

    @GetMapping("fetchById/{id}")
    public ResponseEntity<Optional<CommentDTO>> fetchCommentById(@PathVariable long id){
        return commentClient.externalFetchById(id);
    }

    @GetMapping("fetchCommentsByUserAndBook/user/{userId}/book/{bookId}")
    public ResponseEntity<Optional<List<CommentDTO>>> fetchCommentsByUserAndBook(@PathVariable Long userId, @PathVariable Long bookId){
        log.info("userId: {}, bookId: {}", userId, bookId);
        return commentService.fetchByUserIdAndBookId(userId, bookId);
    }
    //endregion GET

    //region POST
    @PostMapping("/saveOneComment")
    public ResponseEntity<Optional<CommentDTO>> saveOneComment(@RequestBody CommentDTO commentDTO){
        return commentService.saveById(commentDTO);
    }
    //endregion POST

    //region PUT
    @PutMapping("/updateComment/user/{userId}/book/{bookId}/comment/{commentId}")
    public ResponseEntity<Optional<CommentDTO>> updateComment(@PathVariable Long userId,
                                                              @PathVariable Long bookId,
                                                              @PathVariable Long commentId,
                                                              @RequestBody CommentDTO updateComment){
        return commentService.updateComment(userId, bookId, commentId, updateComment);
    }
    //endregion PUT
}