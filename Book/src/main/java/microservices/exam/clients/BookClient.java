package microservices.exam.clients;

import microservices.exam.apiResponse.ApiResponse;

import microservices.exam.apiResponse.ApiResponseBuilder;
import microservices.exam.dtos.ApiResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import microservices.exam.dtos.CommentDTO;
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
public class BookClient {

    private final String restServiceUrl;
    private final RestTemplate restTemplate;

    public BookClient(
            RestTemplateBuilder restTemplateBuilder,
            @Value("http://localhost:8080") final String url
    ){
        this.restTemplate = restTemplateBuilder.build();
        this.restServiceUrl = url;
    }


    public ApiResponse<List<CommentDTO>> externalComment() {
        String url = restServiceUrl + "/comment/fetchAll";
        ResponseEntity<ApiResponseDTO<List<CommentDTO>>> response;

        try {
            log.info("We are here");
            response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );
        }catch (Exception exception){
            log.error(exception.getMessage());
            exception.printStackTrace();
            return null;
        }

        log.info(String.valueOf(HttpStatus.valueOf(response.getStatusCode().value())));
        return new ApiResponseBuilder<List<CommentDTO>>().parseDto(response.getBody(), HttpStatus.valueOf(response.getStatusCode().value()));
    }
//    public List<CommentDTO> externalComment() {
//        String url = restServiceUrl + "/comment/fetchAll";
//        ResponseEntity<List<CommentDTO>> response;
//
//        try {
//            log.info("We are here");
//            response = restTemplate.exchange(
//                    url,
//                    HttpMethod.GET,
//                    null,
//                    new ParameterizedTypeReference<>() {
//                    }
//            );
//
//        } catch (Exception exception) {
//            log.error(exception.getMessage());
//            exception.printStackTrace();
//            return null;
//        }
//        return response.getBody();
//    }
        return response.getBody();
    }

}
