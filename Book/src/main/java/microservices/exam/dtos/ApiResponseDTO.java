package microservices.exam.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponseDTO<T> {
    private T value;
    private String errorMessage;
}
