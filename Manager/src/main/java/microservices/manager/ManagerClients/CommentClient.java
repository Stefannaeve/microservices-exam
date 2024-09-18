package microservices.manager.ManagerClients;

import lombok.extern.slf4j.Slf4j;
import microservices.manager.apiResponse.ApiResponse;
import microservices.manager.apiResponse.ApiResponseBuilder;
import microservices.manager.dtos.ApiResponseDTO;
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

@Slf4j
@Service
public class CommentClient {

    private final String restServiceUrl;
    private final RestTemplate restTemplate;

    public CommentClient(
            RestTemplateBuilder restTemplateBuilder,
            @Value("http://localhost:8081") final String url
    ){
        this.restTemplate = restTemplateBuilder.build();
        this.restServiceUrl = url;
    }

    public ApiResponse<List<CommentDTO>> externalGetAllComments(){
        String url = restServiceUrl + "/comment/fetchAll";
        log.debug("This is the url: {}", url);
        ResponseEntity<ApiResponseDTO<List<CommentDTO>>> response;
        ApiResponseBuilder<List<CommentDTO>> apiResponseBuilder = new ApiResponseBuilder<>();

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
            return apiResponseBuilder.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to connect to comment service");
        }

        if (response.getBody() == null){
            log.error("Response body is null");
            return apiResponseBuilder.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Empty response from comment service");
        }

        HttpStatus statusCode = HttpStatus.valueOf(response.getStatusCode().value());

        log.debug("Received response with status: {}", statusCode);
        return apiResponseBuilder.parseDto(response.getBody(), statusCode);
    }
}
