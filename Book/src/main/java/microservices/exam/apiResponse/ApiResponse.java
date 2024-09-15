package microservices.exam.apiResponse;

import java.util.List;
import java.util.Optional;

public sealed interface ApiResponse {
    record Success<T>(Optional<T> value) implements ApiResponse {}
    record Failure<T>(Optional<T> value, String errorMessage) implements ApiResponse {}
}
