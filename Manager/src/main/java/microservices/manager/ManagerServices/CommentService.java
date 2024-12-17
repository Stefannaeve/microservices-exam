package microservices.manager.ManagerServices;

import lombok.extern.slf4j.Slf4j;
import microservices.manager.ManagerClients.BookClient;
import microservices.manager.ManagerClients.CommentClient;
import microservices.manager.ManagerClients.UserClient;
import microservices.manager.dtos.BookDTO;
import microservices.manager.dtos.CommentDTO;
import microservices.manager.dtos.UserDTO;
import microservices.manager.responseEntityInitializer.ResponseEntityInitializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    //region GET
    public ResponseEntity<Optional<List<CommentDTO>>> fetchByUserIdAndBookId(Long userId, Long bookId) {
        ResponseEntity<Optional<BookDTO>> book = null;
        ResponseEntity<Optional<List<CommentDTO>>> responseEntityComments = null;
        ResponseEntity<Optional<UserDTO>> user = null;

        // Check if book exists
        try {
            book = bookClient.externalGetBookById(bookId);
            if (book == null || book.getBody().isEmpty()){
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.BAD_REQUEST,
                        false,
                        "Did not find the book",
                        HttpStatus.BAD_REQUEST,
                        Optional.empty()
                );
            }
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }



        // Check of user exists
        try {
            user = userClient.externalGetUserById(userId);
            if (user == null || user.getBody().isEmpty()){
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.BAD_REQUEST,
                        false,
                        "did not find the user",
                        HttpStatus.BAD_REQUEST,
                        Optional.empty()
                );
            }
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }



        try {
            responseEntityComments = commentClient.fetchByUserIdAndBookId(userId, bookId);
        }catch (Exception exception){
            log.error("SaveById Exception block: {}", exception.getMessage());
        }

        return responseEntityComments;
    }
    //endregion GET

    //region POST
    public ResponseEntity<Optional<CommentDTO>> saveById(CommentDTO commentDTO){
        ResponseEntity<Optional<BookDTO>> book = null;
        ResponseEntity<Optional<CommentDTO>> responseEntityComment = null;
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
            if (book == null || book.getBody().isEmpty()){
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.BAD_REQUEST,
                        false,
                        "Did not find the book",
                        HttpStatus.BAD_REQUEST,
                        Optional.empty()
                );
            }
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }


        // Check of user exists
        try {
            user = userClient.externalGetUserById(commentDTO.getUserId());
            if (user == null || user.getBody().isEmpty()){
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.BAD_REQUEST,
                        false,
                        "did not find the user",
                        HttpStatus.BAD_REQUEST,
                        Optional.empty()
                );
            }
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }


        try {
            responseEntityComment = commentClient.saveById(commentDTO);
        } catch (Exception exception){
            log.error("SaveById Exception block: {}", exception.getMessage());
        }

        return responseEntityComment;
    }
    //endregion POST

    //region PUT
    public ResponseEntity<Optional<CommentDTO>> updateComment(Long userId, Long bookId, Long commentId, CommentDTO updateComment) {
        ResponseEntity<Optional<BookDTO>> book = null;
        ResponseEntity<Optional<CommentDTO>> responseEntityComment = null;
        ResponseEntity<Optional<UserDTO>> user = null;

        log.info("2userId: {}, bookId: {}, commentId: {}", userId, bookId, commentId);
        log.info("2userId: {}, bookId: {}, text: {}", updateComment.getUserId(), updateComment.getBookId(), updateComment.getText());

        if (updateComment == null){
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
            book = bookClient.externalGetBookById(bookId);
            if (book == null || book.getBody().isEmpty()){
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.BAD_REQUEST,
                        false,
                        "Did not find the book",
                        HttpStatus.BAD_REQUEST,
                        Optional.empty()
                );
            }
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }


        // Check of user exists
        try {
            user = userClient.externalGetUserById(userId);
            if (user == null || user.getBody().isEmpty()){
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.BAD_REQUEST,
                        false,
                        "did not find the user",
                        HttpStatus.BAD_REQUEST,
                        Optional.empty()
                );
            }
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }


        try {
            responseEntityComment = commentClient.updateComment(userId, bookId, commentId, updateComment);
        } catch (Exception exception){
            log.error("SaveById Exception block: {}", exception.getMessage());
        }

        return responseEntityComment;
    }

    public ResponseEntity<Optional<CommentDTO>> deleteComment(Long userId, Long bookId, Long commentId) {
        ResponseEntity<Optional<BookDTO>> book = null;
        ResponseEntity<Optional<CommentDTO>> responseEntityComment = null;
        ResponseEntity<Optional<UserDTO>> user = null;

        // Check if book exists
        try {
            book = bookClient.externalGetBookById(bookId);
            if (book == null || book.getBody().isEmpty()){
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.BAD_REQUEST,
                        false,
                        "Did not find the book",
                        HttpStatus.BAD_REQUEST,
                        Optional.empty()
                );
            }
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }


        // Check of user exists
        try {
            user = userClient.externalGetUserById(userId);
            if (user == null || user.getBody().isEmpty()){
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.BAD_REQUEST,
                        false,
                        "did not find the user",
                        HttpStatus.BAD_REQUEST,
                        Optional.empty()
                );
            }
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }


        try {
            responseEntityComment = commentClient.deleteComment(userId, bookId, commentId);
        } catch (Exception exception){
            log.error("SaveById Exception block: {}", exception.getMessage());
        }

        return responseEntityComment;
    }
    //endregion PUT
}
