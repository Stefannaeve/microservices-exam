package microservices.exam.service;

import lombok.extern.slf4j.Slf4j;
import microservices.exam.apiResponse.ApiResponse;
import microservices.exam.apiResponse.ApiResponseBuilder;
import microservices.exam.apiResponse.ResponseEntityInitializer;
import microservices.exam.eventDriven.BookEventPublisher;
import microservices.exam.models.Book;
import microservices.exam.repository.BookRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BookService {

    private final BookRepository bookRepository;
    private final BookEventPublisher bookEventPublisher;

    public BookService(BookRepository bookRepository, BookEventPublisher bookEventPublisher) {
        this.bookRepository = bookRepository;
        this.bookEventPublisher = bookEventPublisher;
    }

    public ResponseEntity<Optional<List<Book>>> fetchAll() {
        HttpHeaders headers = new HttpHeaders();
        Optional<List<Book>> books = Optional.empty();

        try {
            books = Optional.of(bookRepository.findAll());
            if (books.get().size() <= 0){
                log.info("Found no books");
                headers.add("Success", "true");
                return ResponseEntityInitializer.NewResponseEntity(HttpStatus.OK, false, "Found no books", HttpStatus.NO_CONTENT, books);
            }
            log.info("Found {} books", books.get().size());

            return ResponseEntityInitializer.NewResponseEntity(HttpStatus.OK, books);
        } catch (Exception exception) {
            log.error("Error fetching books: {}", exception.getMessage());
            return ResponseEntityInitializer.NewResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, false, "An unknown error occurred", exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    public ResponseEntity<Optional<Book>> saveOneBook(Book book) {
        Optional<Book> savedBook = Optional.empty();
        try {

            Book bookFromDatabase = bookRepository.findBookByTitleAndAuthorAndPublishDate(book.getTitle(), book.getAuthor(), book.getPublishDate());
            if (bookFromDatabase == null) {
                savedBook = Optional.of(bookRepository.save(book));
                if (savedBook.isEmpty()) {
                    return ResponseEntityInitializer.NewResponseEntity(HttpStatus.OK, false, "Failed to save book", HttpStatus.INTERNAL_SERVER_ERROR, savedBook);
                }
                log.info("Saved book with id: {}", savedBook.get().getId());

                bookEventPublisher.publishBookCreatedEvent(
                        savedBook.get().getId(),
                        savedBook.get().getTitle(),
                        savedBook.get().getAuthor(),
                        savedBook.get().getPages(),
                        savedBook.get().getPublishDate(),
                        savedBook.get().getBookContent()
                );

                return ResponseEntityInitializer.NewResponseEntity(HttpStatus.CREATED, savedBook);
            } else {
                log.info("Book from database: {}", bookFromDatabase.getTitle());
                return ResponseEntityInitializer.NewResponseEntity(HttpStatus.OK, false, "Book already exists", HttpStatus.CONFLICT, savedBook);
            }
        } catch (Exception e) {
            log.error("Error saving book: {}", e.getMessage());
            return ResponseEntityInitializer.NewResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, false, "An unknown error occurred", HttpStatus.INTERNAL_SERVER_ERROR, savedBook);
        }
    }

    public ApiResponse<Void> deleteBookById(Long bookId) {
        ApiResponseBuilder<Void> apiResponseBuilder = new ApiResponseBuilder<>();
        try {
            Optional<Book> book = bookRepository.findById(bookId);
            if (book.isPresent()) {
                bookRepository.delete(book.get());
                log.info("Deleted book with id: {}", bookId);

                bookEventPublisher.publishBookDeletedEvent(bookId);
                return apiResponseBuilder.success(null);
            } else {
                log.warn("Book with id {} not found for deletion", bookId);
                return apiResponseBuilder.failure(HttpStatus.NOT_FOUND, "Book not found");
            }
        } catch (Exception e) {
            log.error("Error deleting book with id {}: {}", bookId, e.getMessage());
            return apiResponseBuilder.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete book");
        }
    }

    public ApiResponse<Book> fetchById(long id) {
        ApiResponseBuilder<Book> apiResponseBuilder = new ApiResponseBuilder<>();
        Optional<Book> book;

        try {
            book = bookRepository.findById(id);
        } catch (Exception exception) {
            log.error("Book service, service, error: {}", exception.getMessage());
            return apiResponseBuilder.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong...");
        }
        if (book.isEmpty()){
            return apiResponseBuilder.failure(HttpStatus.NO_CONTENT, "Book not found");
        }
        log.info("Book found in database");
        return apiResponseBuilder.success(book.get());
    }


    public ApiResponse<List<Book>> fetchBooksByTitle(String title) {
        ApiResponseBuilder<List<Book>> apiResponseBuilder = new ApiResponseBuilder<>();

        try {
            List<Book> books = bookRepository.findByTitle(title);
            if (!books.isEmpty()) {
                log.info("Found {} books with title containing: {}", books.size(), title);
                return apiResponseBuilder.success(books);
            } else {
                log.warn("No books found with title containing: {}", title);
                return apiResponseBuilder.failure(HttpStatus.NOT_FOUND, "No books found with the specified title");
            }
        } catch (Exception e) {
            log.error("Error fetching books by title {}: {}", title, e.getMessage());
            return apiResponseBuilder.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch books by title");
        }
    }
}
