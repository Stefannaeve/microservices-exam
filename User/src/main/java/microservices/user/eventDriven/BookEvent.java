package microservices.user.eventDriven;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

//Message payload
@Getter
@Setter
@AllArgsConstructor
public class BookEvent {
    private Long bookId;        // Which book is this event about?
    private String eventType;   // What action? ("CREATE", "UPDATE", "DELETE")
    private String title;       // (Optional) Book title
    private String author;      // (Optional) Book author
    private Integer pages;      // (Optional) Number of pages
    private String otherDetails; // (Optional) Extra details about the book
}
