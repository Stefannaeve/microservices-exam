package microservices.exam.eventDriven;

import lombok.Value;
import java.util.Date;

@Value
public class BookEvent {
    Long bookId;
    String title;
    String author;
    int pages;
    Date publishDate;
    String bookContent;
}
