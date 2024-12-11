package microservices.comment.eventDriven;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommentEvent {
    private Long id;
    private String eventType;
    private Long userId;
    private Long bookId;
    private String text;

    // Constructor for CREATE event
    public CommentEvent(Long commentId, String eventType, Long userId, Long bookId) {
        this.id = commentId;
        this.eventType = eventType;
        this.userId = userId;
        this.bookId = bookId;
    }

    // Constructor for DELETE event
    public CommentEvent(Long commentId, String eventType) {
        this.id = commentId;
        this.eventType = eventType;
    }

    // Constructor for UPDATE event
    public CommentEvent(Long commentId, String eventType, String text) {
        this.id = commentId;
        this.eventType = eventType;
        this.text = text;
    }
}
