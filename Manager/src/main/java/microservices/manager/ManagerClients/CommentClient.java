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
        log.info("This is the url: " + url);
        ResponseEntity<ApiResponseDTO<List<CommentDTO>>> response;

        try {
            response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );
        } catch (Exception exception){
            log.error(exception.getMessage());
            exception.printStackTrace();
            return null;
        }
        log.info("Http code: " + HttpStatus.valueOf(response.getStatusCode().value()));
        log.info("Response: " + response);
        return new ApiResponseBuilder<List<CommentDTO>>().parseDto(response.getBody(), HttpStatus.valueOf(response.getStatusCode().value()));
    }
}
