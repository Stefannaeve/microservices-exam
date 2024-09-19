package microservices.manager.ManagerControllers;

import lombok.extern.slf4j.Slf4j;
import microservices.manager.ManagerClients.CommentClient;
import microservices.manager.apiResponse.ApiResponse;
import microservices.manager.dtos.CommentDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

}
