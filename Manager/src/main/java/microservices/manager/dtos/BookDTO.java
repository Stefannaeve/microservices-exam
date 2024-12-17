package microservices.manager.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private int pages;
    private String readingProgress;
    private String readingStatus;
    private LocalDate publishDate;
    private String bookContent;
    private int rating;

    @Override
    public String toString() {
        return "BookDTO{" +
                "id=" + id +
                ", title='" + title +
                ", author='" + author +
                ", pages=" + pages +
                ", readingProgress=" + readingProgress +
                ", readingStatus=" + readingStatus +
                ", publishDate=" + publishDate +
                ", bookContent='" + bookContent +
                ", rating='" + rating +
                '}';
    }
}