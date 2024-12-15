package microservices.manager.ManagerClients;

import lombok.extern.slf4j.Slf4j;
import microservices.manager.apiResponse.ResponseEntityInitializer;
import microservices.manager.dtos.CommentDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CommentClient {

    private final String restServiceUrl;
    private final RestTemplate restTemplate;

    //region GET
    public CommentClient(
            RestTemplateBuilder restTemplateBuilder,
            @Value("http://comment:8081") final String url
    ){
        this.restTemplate = restTemplateBuilder.build();
        this.restServiceUrl = url;
    }

    public ResponseEntity<Optional<List<CommentDTO>>> externalFetchAllComments(){
        String url = restServiceUrl + "/comment/fetchAll";
        log.debug("This is the url: {}", url);
        ResponseEntity<Optional<List<CommentDTO>>> response = null;

        try {
            response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );
        } catch (Exception exception){
            log.error("An unexpected error occurred: ", exception);
        }

        if (response == null){
            log.error("Response body is null");
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.NO_CONTENT,
                    false,
                    "Response empty from the comment service",
                    HttpStatus.NO_CONTENT,
                    Optional.empty()
            );
        }

        String success = ResponseEntityInitializer.extractHeader(response, "success");
        log.info("Success: {}", success);

        if (success.equals("false")){
            String errorMessage = ResponseEntityInitializer.extractHeader(response, "ErrorMessage");
            String errorStatus = ResponseEntityInitializer.extractHeader(response, "ErrorStatus");
            HttpStatus status = HttpStatus.valueOf(Integer.parseInt(errorStatus));
            return ResponseEntityInitializer.NewResponseEntity(
                    status,
                    false,
                    errorMessage,
                    status,
                    response.getBody()
            );
        }

        return response;
    }

    public ResponseEntity<Optional<CommentDTO>> externalFetchById(long id) {
        String url = restServiceUrl + "/comment/fetchCommentById/" + id;
        log.debug("This is the url: {}", url);
        ResponseEntity<Optional<CommentDTO>> response = null;

        try {
            response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );
        } catch (Exception exception){
            log.error("An unexpected error occurred: ", exception);
        }

        if (response == null){
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.NO_CONTENT,
                    false,
                    "Response empty from the book service",
                    HttpStatus.NO_CONTENT,
                    Optional.empty()
            );
        }

        String success = ResponseEntityInitializer.extractHeader(response, "success");
        log.info("Success: {}", success);

        if (success.equals("false")){
            String errorMessage = ResponseEntityInitializer.extractHeader(response, "ErrorMessage");
            String errorStatus = ResponseEntityInitializer.extractHeader(response, "ErrorStatus");
            HttpStatus status = HttpStatus.valueOf(Integer.parseInt(errorStatus));
            return ResponseEntityInitializer.NewResponseEntity(
                    status,
                    false,
                    errorMessage,
                    status,
                    response.getBody()
            );
        }

        return response;
    }

    public ResponseEntity<Optional<List<CommentDTO>>> fetchByUserIdAndBookId(Long userId, Long bookId){
        String url = restServiceUrl + "/comment/user/" + userId + "/book/" + bookId;
        log.info("This is the url: {}", url);
        ResponseEntity<Optional<List<CommentDTO>>> response = null;

        try {
            response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );
        } catch (Exception exception){
            log.error("An unexpected error occurred: ", exception);
        }

        if (response == null){
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.NO_CONTENT,
                    false,
                    "Response empty from the book service",
                    HttpStatus.NO_CONTENT,
                    Optional.empty()
            );
        }

        String success = ResponseEntityInitializer.extractHeader(response, "success");
        log.info("Success: {}", success);

        if (success.equals("false")){
            String errorMessage = ResponseEntityInitializer.extractHeader(response, "ErrorMessage");
            String errorStatus = ResponseEntityInitializer.extractHeader(response, "ErrorStatus");
            HttpStatus status = HttpStatus.valueOf(Integer.parseInt(errorStatus));
            return ResponseEntityInitializer.NewResponseEntity(
                    status,
                    false,
                    errorMessage,
                    status,
                    response.getBody()
            );
        }

        return response;
    }
    //endregion GET

    //region POST
    public ResponseEntity<Optional<CommentDTO>> saveById(CommentDTO commentDTO) {
        String url = restServiceUrl + "/comment/saveOneComment";
        log.debug("This is the url: {}", url);
        ResponseEntity<Optional<CommentDTO>> response = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CommentDTO> requestEntity = new HttpEntity<>(commentDTO, headers);

        try {
            response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<>() {
                    }
            );
        } catch (Exception exception){
            log.error("An unexpected error occurred: ", exception);
        }

        if (response == null){
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.NO_CONTENT,
                    false,
                    "Response empty from the book service",
                    HttpStatus.NO_CONTENT,
                    Optional.empty()
            );
        }

        String success = ResponseEntityInitializer.extractHeader(response, "success");
        log.info("Success: {}", success);

        if (success.equals("false")){
            String errorMessage = ResponseEntityInitializer.extractHeader(response, "ErrorMessage");
            String errorStatus = ResponseEntityInitializer.extractHeader(response, "ErrorStatus");
            HttpStatus status = HttpStatus.valueOf(Integer.parseInt(errorStatus));
            return ResponseEntityInitializer.NewResponseEntity(
                    status,
                    false,
                    errorMessage,
                    status,
                    response.getBody()
            );
        }

        return response;
    }
    //endregion POST

    //region PUT
    public ResponseEntity<Optional<CommentDTO>> updateComment(Long userId, Long bookId, Long commentId, CommentDTO updateComment) {
        String url = restServiceUrl + "/comment/updateComment/user/" + userId + "/book/" + bookId + "/comment/" + commentId;
        log.info("userId: {}, bookId: {}, commentId: {}", userId, bookId, commentId);
        log.info("userId: {}, bookId: {}, text: {}", updateComment.getUserId(), updateComment.getBookId(), updateComment.getText());
        log.info("This is the url: {}", url);
        ResponseEntity<Optional<CommentDTO>> response = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CommentDTO> requestEntity = new HttpEntity<>(updateComment, headers);

        try {
            response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    requestEntity,
                    new ParameterizedTypeReference<>() {
                    }
            );
        } catch (Exception exception){

            log.error("An unexpected error occurred: ", exception);
        }

        if (response == null){
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.NO_CONTENT,
                    false,
                    "Response empty from the book service",
                    HttpStatus.NO_CONTENT,
                    Optional.empty()
            );
        }

        String success = ResponseEntityInitializer.extractHeader(response, "success");
        log.info("Success: {}", success);

        if (success.equals("false")){
            String errorMessage = ResponseEntityInitializer.extractHeader(response, "ErrorMessage");
            String errorStatus = ResponseEntityInitializer.extractHeader(response, "ErrorStatus");
            HttpStatus status = HttpStatus.valueOf(Integer.parseInt(errorStatus));
            return ResponseEntityInitializer.NewResponseEntity(
                    status,
                    false,
                    errorMessage,
                    status,
                    response.getBody()
            );
        }

        return response;
    }
    //endregion PUT

    //region DELETE
    public ResponseEntity<Optional<CommentDTO>> deleteComment(Long userId, Long bookId, Long commentId) {
        String url = restServiceUrl + "/comment/delete/user/" + userId + "/book/" + bookId + "/comment/" + commentId;
        log.info("This is the url: {}", url);
        ResponseEntity<Optional<CommentDTO>> response = null;

        try {
            response = restTemplate.exchange(
                    url,
                    HttpMethod.DELETE,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );
        } catch (Exception exception){
            log.error("An unexpected error occurred: ", exception);
        }

        if (response == null){
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.NO_CONTENT,
                    false,
                    "Response empty from the book service",
                    HttpStatus.NO_CONTENT,
                    Optional.empty()
            );
        }

        String success = ResponseEntityInitializer.extractHeader(response, "success");
        log.info("Success: {}", success);

        if (success.equals("false")){
            String errorMessage = ResponseEntityInitializer.extractHeader(response, "ErrorMessage");
            String errorStatus = ResponseEntityInitializer.extractHeader(response, "ErrorStatus");
            HttpStatus status = HttpStatus.valueOf(Integer.parseInt(errorStatus));
            return ResponseEntityInitializer.NewResponseEntity(
                    status,
                    false,
                    errorMessage,
                    status,
                    response.getBody()
            );
        }

        return response;
    }
    //endregion DELETE
}
