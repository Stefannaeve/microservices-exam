package microservices.user.eventDriven;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// Message payload
@Getter
@Setter
@AllArgsConstructor
public class BookEvent {
    private Long bookId;
    private String eventType;
    private String title;
    private String author;
    private Integer pages;
    private String otherDetails;

    @Override
    public String toString() {
        return "BookEvent{" +
                "bookId=" + bookId +
                ", eventType='" + eventType + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", pages=" + pages +
                ", otherDetails='" + otherDetails + '\'' +
                '}';
    }
}
