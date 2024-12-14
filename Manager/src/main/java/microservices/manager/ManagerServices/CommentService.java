package microservices.manager.ManagerServices;

import lombok.extern.slf4j.Slf4j;
import microservices.manager.ManagerClients.BookClient;
import microservices.manager.ManagerClients.CommentClient;
import microservices.manager.ManagerClients.UserClient;
import microservices.manager.apiResponse.ResponseEntityInitializer;
import microservices.manager.dtos.BookDTO;
import microservices.manager.dtos.CommentDTO;
import microservices.manager.dtos.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
        ResponseEntity<Optional<CommentDTO>> comment = null;
        ResponseEntity<Optional<UserDTO>> user = null;

        if (commentDTO == null){
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.OK,
                    false,
                    "Comment from client is null",
                    HttpStatus.BAD_REQUEST,
                    Optional.empty()
            );
        }

        // Check if book exists
        try {
            book = bookClient.externalGetBookById(commentDTO.getBookId());
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }

        if (book == null || book.getBody().isEmpty()){
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.BAD_REQUEST,
                    false,
                    "Did not find the book",
                    HttpStatus.BAD_REQUEST,
                    Optional.empty()
            );
        }

        // Check of user exists
        try {
            user = userClient.externalGetUserById(commentDTO.getUserId());
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }

        if (user == null || user.getBody().isEmpty()){
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.BAD_REQUEST,
                    false,
                    "did not find the user",
                    HttpStatus.BAD_REQUEST,
                    Optional.empty()
            );
        }

        try {
            comment = commentClient.saveById(commentDTO);
        } catch (Exception exception){
            log.error("SaveById Exception block: {}", exception.getMessage());
        }

        return ResponseEntityInitializer.NewResponseEntity(
                HttpStatus.OK,
                comment.getBody()
        );
    }
}
