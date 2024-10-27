package microservices.manager.apiResponse;

import microservices.manager.dtos.ApiResponseDTO;
import org.springframework.http.HttpStatus;

import java.util.Optional;

public class ApiResponseBuilder<T> {
    public ApiResponse<T> success(T value){
        return new ApiResponse.Success<T>(Optional.of(value));
    }

    public ApiResponse<T> success(){
        return new ApiResponse.Success<T>(Optional.empty());
    }

    public ApiResponse<T> failure(T value, HttpStatus status,  String errorMessage){
        return new ApiResponse.Failure<T>(Optional.of(value), status, errorMessage);
    }

    public ApiResponse<T> failure(HttpStatus status, String errorMessage){
        return new ApiResponse.Failure<T>(Optional.empty(), status, errorMessage);
    }

    public ApiResponse<T> parseDto(ApiResponseDTO<T> apiResponseDTO, HttpStatus status){
        if (apiResponseDTO.getErrorMessage() == null){
            if (apiResponseDTO.getValue() != null){
                return success(apiResponseDTO.getValue());
            } else {
                return success();
            }
        } else {
            if (apiResponseDTO.getValue() != null){
                return failure(apiResponseDTO.getValue(), status, apiResponseDTO.getErrorMessage());
            } else {
                return failure(status, apiResponseDTO.getErrorMessage());
            }
        }
    }
}