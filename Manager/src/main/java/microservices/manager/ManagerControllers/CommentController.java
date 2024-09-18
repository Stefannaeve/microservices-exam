package microservices.manager.ManagerControllers;

import lombok.extern.slf4j.Slf4j;
import microservices.manager.ManagerClients.CommentClient;
import microservices.manager.apiResponse.ApiResponse;
import microservices.manager.dtos.CommentDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/manager/comment")
public class CommentController {
    CommentClient commentClient;

    public CommentController(CommentClient commentClient){
        this.commentClient = commentClient;
    }

    @GetMapping("/fetchAll")
    public ResponseEntity<ApiResponse<List<CommentDTO>>> fetchAllComments(){
        ApiResponse<List<CommentDTO>> comments = commentClient.externalGetAllComments();
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }

    @GetMapping("fetchById/{id}")
    public ResponseEntity<ApiResponse<CommentDTO>> fetchCommentById(@PathVariable long id){
        ApiResponse<CommentDTO> commentResponse = commentClient.fetchById(id);

        switch (commentResponse) {
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

}
