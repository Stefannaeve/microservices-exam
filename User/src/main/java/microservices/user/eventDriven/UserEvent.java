package microservices.user.eventDriven;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import microservices.user.models.UserBook;

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
    private UserBook userBook;
    private int rating;

    // delete
    public UserEvent(Long userId, String eventType) {
        this.userId = userId;
        this.eventType = eventType;
    }

    // create
    public UserEvent(Long userId, String eventType, String username) {
        this.userId = userId;
        this.eventType = eventType;
        this.username = username;
    }

    // delete book
    public UserEvent(Long userId, String eventType, Long bookId) {
        this.userId = userId;
        this.eventType = eventType;
        this.bookId = bookId;
    }

    // add book
    public UserEvent(Long userId, String eventType, UserBook userBook) {
        this.userId = userId;
        this.eventType = eventType;
        this.userBook = userBook;
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
                ", userBook=" + userBook +
                '}';
    }
}
