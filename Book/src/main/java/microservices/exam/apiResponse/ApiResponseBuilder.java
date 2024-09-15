package microservices.exam.apiResponse;

import java.util.*;

public class ApiResponseBuilder {
    public static <T> ApiResponse Success(T value){
        return new ApiResponse.Success<T>(Optional.of(value));
    }

    public static <T> ApiResponse Success(){
        return new ApiResponse.Success<T>(Optional.empty());
    }

    public static <T> ApiResponse Failure(T value, String errorMessage){
        return new ApiResponse.Failure<T>(Optional.of(value), errorMessage);
    }

    public static <T> ApiResponse Failure(String errorMessage){
        return new ApiResponse.Failure<T>(Optional.empty(), errorMessage);
    }
}