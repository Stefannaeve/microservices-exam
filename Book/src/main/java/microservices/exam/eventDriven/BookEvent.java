package microservices.exam.eventDriven;

import lombok.Value;
import java.util.Date;

@Value
public class BookEvent {
    Long bookId;
    String eventType;
    String title;
    String author;
    int pages;
    Date publishDate;
    String bookContent;

    // Constructor for delete events
    public BookEvent(Long bookId, String eventType) {
        this.bookId = bookId;
        this.eventType = eventType;
        this.title = null;
        this.author = null;
        this.pages = 0;
        this.publishDate = null;
        this.bookContent = null;
    }

    // Constructor for create and update events
    public BookEvent(Long bookId, String eventType, String title, String author, int pages, Date publishDate, String bookContent) {
        this.bookId = bookId;
        this.eventType = eventType;
        this.title = title;
        this.author = author;
        this.pages = pages;
        this.publishDate = publishDate;
        this.bookContent = bookContent;
    }
}
