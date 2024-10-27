package microservices.exam.repository;

import microservices.exam.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Book findBookByTitleAndAuthorAndPublishDate(String title, String Author, Date date);

}
