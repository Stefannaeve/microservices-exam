package microservices.exam.utils;

import java.util.Optional;

public class ApiResponse<T> {
    public boolean isSuccess;
    public Optional<T> Value;
    public String ErrorMessage;

    private ApiResponse(boolean isSuccess, Optional<T> value, String errorMessage) {
        this.isSuccess = isSuccess;
        Value = value;
        ErrorMessage = errorMessage;
    }

    private ApiResponse(boolean isSuccess, Optional<T> value) {
        this.isSuccess = isSuccess;
        Value = value;
    }

    private ApiResponse(boolean isSuccess, String errorMessage) {
        this.isSuccess = isSuccess;
        ErrorMessage = errorMessage;
    }

    private ApiResponse(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public static <T>ApiResponse Success(Optional<T> value) {
        return new ApiResponse<T>(true, value);
    }

    public static <T>ApiResponse Success() {
        return new ApiResponse<T>(true);
    }

    public static <T>ApiResponse Failure(String errorMessage) {
        return new ApiResponse<T>(false, errorMessage);
    }

    public static <T>ApiResponse Failure(Optional<T> value, String errorMessage) {
        return new ApiResponse<T>(true, value, errorMessage);
    }
}
