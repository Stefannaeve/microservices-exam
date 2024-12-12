package microservices.manager.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDTO {
    private Long id;
    private Long userId;
    private Long bookId;
    private int page;
    private String text;
}
