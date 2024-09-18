package microservices.manager.ManagerControllers;

import lombok.extern.slf4j.Slf4j;
import microservices.manager.ManagerClients.CommentClient;
import microservices.manager.apiResponse.ApiResponse;
import microservices.manager.dtos.CommentDTO;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/fetchAllComments")
    public ResponseEntity<ApiResponse<List<CommentDTO>>> fetchAllComments(){
        ApiResponse<List<CommentDTO>> comments = commentClient.externalGetAllComments();
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }

}
