package microservices.manager.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDTO {

    private Long id;
    private String username;

    @JsonProperty("books")
    private List<BookDTO> userBooks;
}
