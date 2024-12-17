
package microservices.exam.eventDriven;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;


@Getter
@Setter
public class BookEvent {
    private Long bookId;
    private String eventType;
    private String title;
    private String author;
    private int pages;
    private LocalDate publishDate;
    private String bookContent;

    // delete events
    public BookEvent(Long bookId, String eventType) {
        this.bookId = bookId;
        this.eventType = eventType;
        this.title = null;
        this.author = null;
        this.pages = 0;
        this.publishDate = null;
        this.bookContent = null;
    }

    // create and update events
    public BookEvent(Long bookId, String eventType, String title, String author, int pages, LocalDate publishDate, String bookContent) {
        this.bookId = bookId;
        this.eventType = eventType;
        this.title = title;
        this.author = author;
        this.pages = pages;
        this.publishDate = publishDate;
        this.bookContent = bookContent;
    }

    @Override
    public String toString() {
        return "BookEvent{" +
                "bookId=" + bookId +
                ", eventType='" + eventType + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", pages=" + pages +
                ", publishDate=" + publishDate +
                ", bookContent='" + bookContent + '\'' +
                '}';
    }
}
