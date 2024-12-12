package microservices.manager.ManagerServices;

import lombok.extern.slf4j.Slf4j;
import microservices.manager.ManagerClients.BookClient;
import microservices.manager.ManagerClients.CommentClient;
import microservices.manager.ManagerClients.UserClient;
import microservices.manager.apiResponse.ApiResponse;
import microservices.manager.apiResponse.ApiResponseBuilder;
import microservices.manager.dtos.BookDTO;
import microservices.manager.dtos.CommentDTO;
import microservices.manager.dtos.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public ApiResponse<CommentDTO> saveById(CommentDTO commentDTO){
        ApiResponseBuilder<CommentDTO> apiResposeBuilder = new ApiResponseBuilder<>();
        if (commentDTO == null){
            return apiResposeBuilder.failure(HttpStatus.BAD_REQUEST, "Comment is empty");
        }

        // Check if book exists
        try {
            ApiResponse<BookDTO> book = bookClient.externalGetBookById(commentDTO.getBookId());
            switch (book){
                case ApiResponse.Success<BookDTO> success -> {

                }
                case ApiResponse.Failure<BookDTO> failure -> {
                    log.debug(failure.errorMessage());
                    return apiResposeBuilder.failure(failure.status(), failure.errorMessage());
                }
            }
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return apiResposeBuilder.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong...");
        }

        // Check of user exists
        try {
            ApiResponse<UserDTO> user = userClient.externalGetUserById(commentDTO.getUserId());
            switch (user){
                case ApiResponse.Success<UserDTO> success -> {

                }
                case ApiResponse.Failure<UserDTO> failure -> {
                    log.debug(failure.errorMessage());
                    return apiResposeBuilder.failure(failure.status(), failure.errorMessage());
                }
            }
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return apiResposeBuilder.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong...");
        }

        ApiResponse<CommentDTO> comment = commentClient.saveById(commentDTO);
        return comment;
    }
}
