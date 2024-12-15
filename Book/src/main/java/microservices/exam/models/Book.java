package microservices.exam.models;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    public Book(String title, String author, int pages, String bookContent) {
        this.title = title;
        this.author = author;
        this.pages = pages;
        this.bookContent = bookContent;
    }

    @Id
    @GeneratedValue(generator = "model_generator")
    @SequenceGenerator(name = "model_generator", sequenceName = "model_seq", initialValue = 1, allocationSize = 1)
    @Column(name = "id")
    @CsvBindByName(column = "Text#", required = true)
    private Long id;

    @Column(name = "title")
    @CsvBindByName(column = "Title")
    private String title;

    @Column(name = "Author")
    @CsvBindByName(column = "Authors")
    private String author;

    @Column(name = "Pages")
    private int pages;

    @Column(name = "publish_date")
    @CsvDate(value ="yyyy-MM-dd")
    @CsvBindByName(column = "Issued")
    private LocalDate publishDate;

    @Lob
    @Column(name = "book_content", columnDefinition = "LONGTEXT")
    private String bookContent;

    // TODO: State management
    // Genres, International Standard Book Number
}
