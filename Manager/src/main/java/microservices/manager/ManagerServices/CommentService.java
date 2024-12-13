package microservices.manager.ManagerServices;

import lombok.extern.slf4j.Slf4j;
import microservices.manager.ManagerClients.BookClient;
import microservices.manager.ManagerClients.CommentClient;
import microservices.manager.ManagerClients.UserClient;
import microservices.manager.apiResponse.ApiResponse;
import microservices.manager.apiResponse.ApiResponseBuilder;
import microservices.manager.apiResponse.ResponseEntityInitializer;
import microservices.manager.dtos.BookDTO;
import microservices.manager.dtos.CommentDTO;
import microservices.manager.dtos.UserDTO;
import org.apache.coyote.Response;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CommentService {
    private final CommentClient commentClient;
    private final BookClient bookClient;
    private final UserClient userClient;

    public CommentService(CommentClient commentClient, BookClient bookClient, UserClient userClient){
        this.commentClient = commentClient;
        this.bookClient = bookClient;
        this.userClient = userClient;
    }

    public ResponseEntity<Optional<CommentDTO>> saveById(CommentDTO commentDTO){
        ResponseEntity<Optional<BookDTO>> book = null;
        ResponseEntity<Optional<CommentDTO>> comment = ResponseEntity.ok(Optional.empty());

        if (commentDTO == null){
            return ResponseEntityInitializer.NewResponseEntity(HttpStatus.OK, false, "Comment from client is null", HttpStatus.BAD_REQUEST, comment.getBody());
        }

        // Check if book exists
        try {
            book = bookClient.externalGetBookById(commentDTO.getBookId());
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }

        if (book != null){
            return ResponseEntityInitializer.NewResponseEntity(HttpStatus.OK, false, "Did not find the book", HttpStatus.BAD_REQUEST, comment.getBody());
        }

        // Check of user exists
        try {
            ApiResponse<UserDTO> user = userClient.externalGetUserById(commentDTO.getUserId());
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }

        //TODO: fix this please
        ApiResponse<CommentDTO> savedComment = commentClient.saveById(commentDTO);

        return ResponseEntityInitializer.NewResponseEntity(HttpStatus.OK, Optional.empty());
    }
}
