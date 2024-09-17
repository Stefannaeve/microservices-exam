package apiResponse;

import org.springframework.http.HttpStatus;

import java.util.Optional;

public sealed interface ApiResponse<T> {
    record Success<K>(Optional<K> value, HttpStatus status) implements ApiResponse<K> {}
    record Failure<K>(Optional<K> value, HttpStatus status, String errorMessage) implements ApiResponse<K> {}
}