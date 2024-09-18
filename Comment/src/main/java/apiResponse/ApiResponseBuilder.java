package apiResponse;

import org.springframework.http.HttpStatus;

import java.util.Optional;

public class ApiResponseBuilder<T> {
    public ApiResponse<T> success(T value, HttpStatus status){
        return new ApiResponse.Success<T>(Optional.of(value), status);
    }

    public ApiResponse<T> success(T value){
        return new ApiResponse.Success<T>(Optional.of(value), HttpStatus.OK);
    }

    public ApiResponse<T> success(){
        return new ApiResponse.Success<T>(Optional.empty(), HttpStatus.OK);
    }

    public ApiResponse<T> failure(T value, HttpStatus status, String errorMessage){
        return new ApiResponse.Failure<T>(Optional.of(value), status, errorMessage);
    }

    public ApiResponse<T> failure(HttpStatus status, String errorMessage){
        return new ApiResponse.Failure<T>(Optional.empty(), status, errorMessage);
    }
}
