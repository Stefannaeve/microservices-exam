package microservices.exam.models;

import jakarta.persistence.*;
import lombok.*;

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
        title.toLowerCase();
    }

    @Id
    @GeneratedValue(generator = "model_generator")
    @SequenceGenerator(name = "model_generator", sequenceName = "model_seq", initialValue = 1, allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "Author")
    private String author;

    @Column(name = "Pages")
    private int pages;

    @Column(name = "publish_date")
    private Date publishDate;

    @Lob
    @Column(name = "book_content", columnDefinition = "LONGTEXT")
    private String bookContent;

    // TODO: State management
    // Genres, International Standard Book Number



}
