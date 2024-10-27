package microservices.exam.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDTO {
    private Long id;
    private int page;
    private String text;
}
