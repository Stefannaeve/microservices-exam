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
    private String username;
    private Long bookId;
    private String readingProgress;
    private String readingStatus;

    // for basic events
    public UserEvent(Long userId, String eventType) {
        this.userId = userId;
        this.eventType = eventType;
    }

    // user creation events
    public UserEvent(Long userId, String eventType, String username) {
        this.userId = userId;
        this.eventType = eventType;
        this.username = username;
    }

    // book deletion events
    public UserEvent(Long userId, String eventType, Long bookId) {
        this.userId = userId;
        this.eventType = eventType;
        this.bookId = bookId;
    }

    // progress update events
    public UserEvent(Long userId, String eventType, Long bookId, String readingProgress, String readingStatus) {
        this.userId = userId;
        this.eventType = eventType;
        this.bookId = bookId;
        this.readingProgress = readingProgress;
        this.readingStatus = readingStatus;
    }

    @Override
    public String toString() {
        return "UserEvent{" +
                "userId=" + userId +
                ", eventType='" + eventType + '\'' +
                ", username='" + username + '\'' +
                ", bookId=" + bookId +
                ", readingProgress='" + readingProgress + '\'' +
                ", readingStatus='" + readingStatus + '\'' +
                '}';
    }
}
