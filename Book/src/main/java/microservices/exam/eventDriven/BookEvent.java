package microservices.exam.eventDriven;

import lombok.Value;
import java.time.LocalDate;

@Value
public class BookEvent {
    Long bookId;
    String title;
    String author;
    int pages;
    LocalDate publishDate;
    String bookContent;
}
