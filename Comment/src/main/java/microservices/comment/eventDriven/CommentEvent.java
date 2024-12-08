package microservices.comment.eventDriven;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommentEvent {
    private Long userId;
    private Long bookId;
    private int page;
    private boolean positive;
    private boolean negative;
    private String text;
}
