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
    private Long userId;
    private String eventType;
    private Long bookId;
    private String readingProgress;
    private String readingStatus;
   
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


