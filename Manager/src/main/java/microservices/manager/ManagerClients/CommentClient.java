package microservices.manager.ManagerClients;

import lombok.extern.slf4j.Slf4j;
import microservices.manager.apiResponse.ResponseEntityInitializer;
import microservices.manager.dtos.CommentDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CommentClient {

    private final String restServiceUrl;
    private final RestTemplate restTemplate;

    public CommentClient(
            RestTemplateBuilder restTemplateBuilder,
            @Value("http://comment:8081") final String url
    ){
        this.restTemplate = restTemplateBuilder.build();
        this.restServiceUrl = url;
    }

    public ResponseEntity<Optional<List<CommentDTO>>> externalGetAllComments(){
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

    public ResponseEntity<Optional<CommentDTO>> fetchById(long id) {
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

    public ResponseEntity<Optional<CommentDTO>> saveById(CommentDTO commentDTO) {
        String url = restServiceUrl + "/comment/fetchCommentById/" + commentDTO.getId();
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
}
