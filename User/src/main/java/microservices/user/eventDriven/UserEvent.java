package microservices.user.eventDriven;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Message payload
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEvent {
    private Long userId;       // Which user does this event affect?
    private String eventType;  // What type of action is this? ("DELETE", "DELETE_BOOK")
    private Long bookId;       // (Optional) If deleting a book, which book?

    public UserEvent(Long userId, String eventType) {
        this.userId = userId;
        this.eventType = eventType;
    }
}
