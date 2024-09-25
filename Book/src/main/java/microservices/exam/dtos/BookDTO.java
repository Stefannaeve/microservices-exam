package microservices.exam.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BookDTO {
    private String title;
    private String author;
    private int pages;
    private Date publishDate;
    private String bookContent;
}
