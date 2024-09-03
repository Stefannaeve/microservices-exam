package microservices.exam.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(generator = "model_generator")
    @SequenceGenerator(name = "model_generator", sequenceName = "model_seq", initialValue = 1, allocationSize = 1)
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "Author")
    private String author;

    @Column(name = "Pages")
    private int pages;

    @Column(name = "publish_date")
    private Date publishDate;

    @Column(name = "book_content")
    private String bookContent;

    // TODO: State management
    // Genres, International Standard Book Number



}
