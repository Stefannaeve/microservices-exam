package microservices.exam.repository;

import microservices.exam.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitle(String title);

    Book findBookByTitleAndAuthorAndPublishDate(String title, String Author, LocalDate date);

    List<Book> findByAuthor(String author);
}
