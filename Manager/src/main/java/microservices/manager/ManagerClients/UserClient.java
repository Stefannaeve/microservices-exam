package microservices.manager.ManagerClients;

import lombok.extern.slf4j.Slf4j;
import microservices.manager.apiResponse.ApiResponse;
import microservices.manager.apiResponse.ApiResponseBuilder;
import microservices.manager.dtos.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.View;

import static java.awt.SystemColor.info;

@Slf4j
@Service
public class UserClient {

    private final String restServiceUrl;
    private final RestTemplate restTemplate;
    private final View error;

    public UserClient(RestTemplateBuilder restTemplateBuilder,
                      @Value("http://user:8082/user") final String url,
                      View error) {
        this.restServiceUrl = url;
        this.restTemplate = restTemplateBuilder.build();
        this.error = error;
    }

    public ApiResponse<UserDTO> externalGetUserById(long userId) {
        ApiResponseBuilder<UserDTO> apiResponseBuilder = new ApiResponseBuilder<>();
        String url = restServiceUrl + "/fetchUserById/" + userId;
        log.error(url);
        UserDTO response;
        try {

            response = restTemplate.getForObject(
                    restServiceUrl + "/fetchUserById/{userId}", UserDTO.class, userId);

        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return null;
        }
        return apiResponseBuilder.success(response);
    }
}
