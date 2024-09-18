
package microservices.exam.service;
import lombok.extern.slf4j.Slf4j;
import microservices.exam.apiResponse.ApiResponseBuilder;
import microservices.exam.apiResponse.ApiResponse;
import microservices.exam.models.Book;
import microservices.exam.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
public class BookService {

    BookRepository bookRepository;

    public BookService(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    public ApiResponse<List<Book>> fetchAll(){
        ApiResponseBuilder<List<Book>> apiResponseBuilder = new ApiResponseBuilder<>();
        List<Book> listBooks;

        try {
            listBooks = bookRepository.findAll();
        } catch (Exception exception){
            log.error(exception.getMessage());
            return apiResponseBuilder.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong...");
        }

        log.info("Found {} books", listBooks.size());
        return apiResponseBuilder.success(listBooks);
    }

    /*
    public List<Book> fetchAll() {
        List<Book> listBooks;

        try {
            listBooks = bookRepository.findAll();
            log.info("Found {} books", listBooks.size());
            return listBooks;
        } catch (Exception exception){
            log.error(exception.getMessage());
            throw exception;
        }
    }
     */

    public ApiResponse<Book> saveOneBook(Book book){
        ApiResponseBuilder<Book> apiResponseBuilder = new ApiResponseBuilder<>();
        try {
            Book bookFromDatabase = bookRepository.findBookByTitleAndAuthorAndPublishDate(book.getTitle(), book.getAuthor(), book.getPublishDate());
            if (bookFromDatabase == null){
                Book savedBook = bookRepository.save(book);
                log.info("Added book with id: {} to the database", savedBook.getId());

                return apiResponseBuilder.success(savedBook);
            } else {
                log.error("Tried adding already existing book, already existing book has id: {}", bookFromDatabase.getId());
                return apiResponseBuilder.failure(HttpStatus.CONFLICT, "Book already exists");
            }
        } catch (Exception exception){
            log.error(exception.getMessage());
            return apiResponseBuilder.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong...");
        }
    }


}
