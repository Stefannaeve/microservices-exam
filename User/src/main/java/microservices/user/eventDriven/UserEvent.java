package microservices.user.eventDriven;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEvent {
    private Long userId;       // The user affected by the event
    private String eventType;  // The type of event (e.g., "DELETE", "DELETE_BOOK", etc.)
    private Long bookId;       // The book associated with the event (if applicable)
    private String readingProgress; // Optional: For update progress events
    private String readingStatus; // Add this field for the status
   
    public UserEvent(Long userId, String eventType) {
        this.userId = userId;
        this.eventType = eventType;
    }

    public UserEvent(Long userId, String eventType, Long bookId) {
        this.userId = userId;
        this.eventType = eventType;
        this.bookId = bookId;
    }
}


