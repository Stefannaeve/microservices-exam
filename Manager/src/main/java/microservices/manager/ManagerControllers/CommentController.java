package microservices.manager.ManagerControllers;

import lombok.extern.slf4j.Slf4j;
import microservices.manager.ManagerClients.CommentClient;
import microservices.manager.ManagerServices.CommentService;
import microservices.manager.apiResponse.ApiResponse;
import microservices.manager.apiResponse.ResponseEntityInitializer;
import microservices.manager.dtos.CommentDTO;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ApiResponse<List<CommentDTO>>> fetchAllComments(){
        ApiResponse<List<CommentDTO>> comments = commentClient.externalGetAllComments();

        switch (comments){
            case ApiResponse.Success<List<CommentDTO>> success -> {
                if (success.value().isPresent()){
                    log.info("Success, returning {} comments from comments service", success.value().get().size());
                } else {
                    log.info("Success");
                }
                return ResponseEntity.status(200).body(success);
            }
            case ApiResponse.Failure<List<CommentDTO>> failure -> {
                log.debug(failure.errorMessage());
                return ResponseEntity.status(failure.status()).body(failure);
            }
        }
    }

    @GetMapping("fetchById/{id}")
    public ResponseEntity<ApiResponse<CommentDTO>> fetchCommentById(@PathVariable long id){
        ApiResponse<CommentDTO> commentResponse = commentClient.fetchById(id);

        switch (commentResponse){
            case ApiResponse.Success<CommentDTO> success -> {
                if (success.value().isPresent()){
                    log.info("Success, comment with id: {} fetched", success.value().get().getId());
                } else {
                    log.info("Success");
                }
                return ResponseEntity.status(HttpStatus.OK).body(success);
            }
            case ApiResponse.Failure<CommentDTO> failure -> {
                log.error(failure.errorMessage());
                return ResponseEntity.status(failure.status()).body(failure);
            }
        }
    }

    @PostMapping("/SaveOneComment")
    public ResponseEntity<Optional<CommentDTO>> saveOneComment(@RequestBody CommentDTO commentDTO){
        ResponseEntity<Optional<CommentDTO>> response = commentService.saveById(commentDTO);
        return response;
    }
}