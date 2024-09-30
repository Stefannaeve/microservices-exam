package microservices.manager.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDTO {

    private Long id;
    private String username;
    private List<BookDTO> userBooks;
}
