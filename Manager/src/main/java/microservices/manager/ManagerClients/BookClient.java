package microservices.manager.ManagerClients;

import lombok.extern.slf4j.Slf4j;
import microservices.manager.responseEntityInitializer.ResponseEntityInitializer;
import microservices.manager.dtos.BookDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.commons.security.ResourceServerTokenRelayAutoConfiguration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
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
            @Value("http://book:8082") final String url,
            ResourceServerTokenRelayAutoConfiguration resourceServerTokenRelayAutoConfiguration) {
        this.restServiceUrl = url;
        this.restTemplate = restTemplateBuilder.build();
    }

    public ResponseEntity<Optional<List<BookDTO>>> externalGetAllBooks() {
        String url = restServiceUrl + "/book/fetchAll";
        log.debug("This is the url: {}", url);
        ResponseEntity<Optional<List<BookDTO>>> response = null;

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

    public ResponseEntity<Optional<BookDTO>> externalGetBookById(long id) {
        String url = restServiceUrl + "/book/fetchBookById/" + id;
        ResponseEntity<Optional<BookDTO>> response = null;

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
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.NO_CONTENT,
                    false,
                    "Response empty from the book service",
                    HttpStatus.NO_CONTENT,
                    Optional.empty());
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


    public ResponseEntity<Optional<BookDTO>> externalDeleteBook(Long bookId){
        String url = restServiceUrl + "/book/delete/" + bookId;
        ResponseEntity<Optional<BookDTO>> response = null;

        log.info("Url: {}", url);

        try {
            response = restTemplate.exchange(
                    url,
                    HttpMethod.DELETE,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );
        } catch (Exception exception) {
            log.error("An unexpected error occurred: ", exception);
        }

        if (response == null){
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.NO_CONTENT,
                    false,
                    "Response empty from the book service",
                    HttpStatus.NO_CONTENT,
                    Optional.empty());
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

    public ResponseEntity<Optional<List<BookDTO>>> externalGetBookByAuthor(String author) {
        String url = restServiceUrl + "/book/fetchByAuthor/" + author;
        ResponseEntity<Optional<List<BookDTO>>> response = null;

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
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.NO_CONTENT,
                    false,
                    "Response empty from the book service",
                    HttpStatus.NO_CONTENT,
                    Optional.empty());
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

    public ResponseEntity<Optional<List<BookDTO>>> externalGetBookByTitle(String title) {
        String url = restServiceUrl + "/book/fetchByTitle/" + title;
        ResponseEntity<Optional<List<BookDTO>>> response = null;

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
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.NO_CONTENT,
                    false,
                    "Response empty from the book service",
                    HttpStatus.NO_CONTENT,
                    Optional.empty());
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
    public ResponseEntity<Optional<List<BookDTO>>> populateDatabaseFromGutenberg(int maxBookCount){
        String url = restServiceUrl + "/book/populateDatabaseFromGutenberg/" + maxBookCount;
        ResponseEntity<Optional<List<BookDTO>>> response = null;

        try {
            response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>(){
                    }
            );
            System.out.println(response);
        } catch (Exception exception) {
            log.error("An unexpected error occurred", exception);
        }

        if (response == null) {
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

    public ResponseEntity<Optional<BookDTO>> fetchBookContentById(Long id){
        String url = restServiceUrl + "/book/fetchBookContentById/" + id;

        ResponseEntity<Optional<BookDTO>> response = null;

        try {
            response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>(){
                    }
            );
        }  catch (Exception exception) {
            log.error("An unexpected error occurred: ", exception);
        }

        if (response == null){
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.NO_CONTENT,
                    false,
                    "Response empty from the book service",
                    HttpStatus.NO_CONTENT,
                    Optional.empty());
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
