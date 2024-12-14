package microservices.manager.ManagerClients;

import lombok.extern.slf4j.Slf4j;
import microservices.manager.apiResponse.ApiResponse;
import microservices.manager.apiResponse.ApiResponseBuilder;
import microservices.manager.apiResponse.ResponseEntityInitializer;
import microservices.manager.dtos.ApiResponseDTO;
import microservices.manager.dtos.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.View;

import java.util.Optional;

import static java.awt.SystemColor.info;

@Slf4j
@Service
public class UserClient {

    private final String restServiceUrl;
    private final RestTemplate restTemplate;
    private final View error;

    public UserClient(RestTemplateBuilder restTemplateBuilder,
                      @Value("http://user:8083/user") final String url,
                      View error) {
        this.restServiceUrl = url;
        this.restTemplate = restTemplateBuilder.build();
        this.error = error;
    }

    public ResponseEntity<Optional<UserDTO>> externalGetUserById(long userId) {
        String url = restServiceUrl + "/fetchUserById/" + userId;
        log.error(url);
        ResponseEntity<Optional<UserDTO>> response = null;
        try {

            //response = restTemplate.getForObject(
            //        restServiceUrl + "/fetchUserById/{userId}", userId);
            response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );

        } catch (Exception exception) {
            log.error("An unexpected error occured: {}", exception.getMessage());

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

    public ResponseEntity<Optional<UserDTO>> externalGetUserWithBook(long userId, long bookId) {
        String url = restServiceUrl + "/fetchUserWithBook/" + userId + "/" + bookId;
        log.error(url);
        ResponseEntity<Optional<UserDTO>> response = null;
        try {

            response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );
        } catch (Exception exception) {
            log.error("An unexpected error occured: {}", exception.getMessage());
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
