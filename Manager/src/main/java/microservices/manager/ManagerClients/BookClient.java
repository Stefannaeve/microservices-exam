package microservices.manager.ManagerClients;

import lombok.extern.slf4j.Slf4j;
import microservices.manager.apiResponse.ApiResponse;
import microservices.manager.apiResponse.ApiResponseBuilder;
import microservices.manager.apiResponse.ResponseEntityInitializer;
import microservices.manager.dtos.ApiResponseDTO;
import microservices.manager.dtos.BookDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BookClient {
    private final String restServiceUrl;
    private final RestTemplate restTemplate;

    public BookClient(
            RestTemplateBuilder restTemplateBuilder,
            @Value("http://book:8082") final String url
    ) {
        this.restServiceUrl = url;
        this.restTemplate = restTemplateBuilder.build();
    }

    public ResponseEntity<List<BookDTO>> externalGetAllBooks() {
        String url = restServiceUrl + "/book/fetchAll";
        log.debug("This is the url: {}", url);
        ResponseEntity<List<BookDTO>> response = null;

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
            return ResponseEntityInitializer.NewResponseEntity(HttpStatus.NO_CONTENT, false, "Response empty from the book service", HttpStatus.NO_CONTENT, response.getBody());
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

        return ResponseEntityInitializer.NewResponseEntity(
                HttpStatus.OK,
                response.getBody()
        );
    }

    public ResponseEntity<Optional<BookDTO>> externalSaveBook(BookDTO book) {
        String url = restServiceUrl + "/book/saveOneBook";
        ResponseEntity<Optional<BookDTO>> response = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<BookDTO> requestEntity = new HttpEntity<>(book, headers);

        try {
            response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<>() {

                    }
            );
        } catch (Exception exception) {
            log.error("An unexpected error occurred: ", exception);
        }

        if (response == null){
            return ResponseEntityInitializer.NewResponseEntity(HttpStatus.NO_CONTENT, false, "Response empty from the book service", HttpStatus.NO_CONTENT, response.getBody());
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

        log.info("Book saved successfully.");
        return response;
    }

    public ResponseEntity<Optional<BookDTO>> externalGetBookById(long id) {
        String url = restServiceUrl + "/book/fetchBookById" + id;
        ResponseEntity<Optional<BookDTO>> response = ResponseEntity.ok(Optional.empty());

        try {
            response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );
        } catch (Exception exception) {
            log.error("An unexpected error occurred: ", exception);
        }

        if (response == null){
            return ResponseEntityInitializer.NewResponseEntity(HttpStatus.NO_CONTENT, false, "Response empty from the book service", HttpStatus.NO_CONTENT, response.getBody());
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

        HttpStatus statusCode = HttpStatus.valueOf(response.getStatusCode().value());

        log.debug("Received response with status: {}", statusCode);
        return response;
    }
}
