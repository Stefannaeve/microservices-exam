
package microservices.comment.eventDriven;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentEvent {
    private Long id;
    private String eventType;
    private Long userId;
    private Long bookId;
    private int page;
    private boolean positive;
    private boolean negative;
    private String text;

    // delete events
    public CommentEvent(Long id, String eventType) {
        this.id = id;
        this.eventType = eventType;
    }

    // create events
    public CommentEvent(Long id, String eventType, Long userId, Long bookId) {
        this.id = id;
        this.eventType = eventType;
        this.userId = userId;
        this.bookId = bookId;
    }

    // update events
    public CommentEvent(Long id, String eventType, String text) {
        this.id = id;
        this.eventType = eventType;
        this.text = text;
    }

    @Override
    public String toString() {
        return "CommentEvent{" +
                "id=" + id +
                ", eventType='" + eventType + '\'' +
                ", userId=" + userId +
                ", bookId=" + bookId +
                ", text='" + text + '\'' +
                '}';
    }
}
