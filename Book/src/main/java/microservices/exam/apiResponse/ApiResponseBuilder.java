package microservices.exam.apiResponse;

import org.springframework.http.HttpStatus;

import java.util.*;

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
}