package microservices.manager.ManagerClients;

import lombok.extern.slf4j.Slf4j;
import microservices.manager.apiResponse.ApiResponse;
import microservices.manager.apiResponse.ApiResponseBuilder;
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

    public ApiResponse<List<BookDTO>> externalGetAllBooks() {
        String url = restServiceUrl + "/book/fetchAll";
        log.debug("This is the url: {}", url);
        ResponseEntity<ApiResponseDTO<List<BookDTO>>> response;
        ApiResponseBuilder<List<BookDTO>> apiResponseBuilder = new ApiResponseBuilder<>();

        try {
            response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );
        }  catch (HttpClientErrorException clientErrorException){
            log.debug("Entered exception handling block");

            HttpStatus status = HttpStatus.valueOf(clientErrorException.getStatusCode().value());

            ApiResponse apiResponse = clientErrorException.getResponseBodyAs(ApiResponse.Failure.class);

            return apiResponse;
        }catch (Exception exception) {
            log.error("An unexpected error occurred: ", exception);
            return apiResponseBuilder.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to connect to book service");
        }

        if (response.getBody() == null) {
            log.error("Response body is null");
            return apiResponseBuilder.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Empty response from book service");
        }

        HttpStatus statusCode = HttpStatus.valueOf(response.getStatusCode().value());

        log.debug("Received response with status: {}", statusCode);
        return apiResponseBuilder.parseDto(response.getBody(), statusCode);
    }

    public ApiResponse<BookDTO> externalSaveBook(BookDTO book) {
        String url = restServiceUrl + "/book/saveOneBook";
        log.debug("This is the url: {}", url);
        ResponseEntity<ApiResponseDTO<BookDTO>> response;
        ApiResponseBuilder<BookDTO> apiResponseBuilder = new ApiResponseBuilder<>();

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
        } catch (HttpClientErrorException clientException) {
            log.debug("Entered exception handling block");

            HttpStatus status = HttpStatus.valueOf(clientException.getStatusCode().value());

            ApiResponse apiResponse = clientException.getResponseBodyAs(ApiResponse.Failure.class);

            return apiResponse;
        } catch (Exception exception) {
            log.error("Error message: {}", exception.getMessage());
            return apiResponseBuilder.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to connect to book service");
        }

        if (response.getBody() == null) {
            log.error("Response body is null");
            return apiResponseBuilder.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Empty response from book service");
        }

        HttpStatus statusCode = HttpStatus.valueOf(response.getStatusCode().value());

        log.debug("Received response with status: {}", statusCode);
        return apiResponseBuilder.parseDto(response.getBody(), statusCode);
    }
}
