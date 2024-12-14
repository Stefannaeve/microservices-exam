package microservices.exam.service;

import lombok.extern.slf4j.Slf4j;
import microservices.exam.apiResponse.ResponseEntityInitializer;
import microservices.exam.eventDriven.BookEventPublisher;
import microservices.exam.models.Book;
import microservices.exam.repository.BookRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
        Optional<List<Book>> books = Optional.empty();

        try {
            books = Optional.of(bookRepository.findAll());
            if (books.get().size() <= 0){
                log.info("Found no books");
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        false,
                        "Found no books",
                        HttpStatus.NO_CONTENT,
                        books
                );
            }
            log.info("Found {} books", books.get().size());

            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.OK,
                    books
            );
        } catch (Exception exception) {
            log.error("Error fetching books: {}", exception.getMessage());
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    false,
                    "An unknown error occurred",
                    exception.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    books
            );
        }
    }

    public ResponseEntity<Optional<Book>> saveOneBook(Book book) {
        Optional<Book> savedBook = Optional.empty();
        try {

            Book bookFromDatabase = bookRepository.findBookByTitleAndAuthorAndPublishDate(book.getTitle(), book.getAuthor(), book.getPublishDate());
            if (bookFromDatabase == null) {
                savedBook = Optional.of(bookRepository.save(book));
                if (savedBook.isEmpty()) {
                    return ResponseEntityInitializer.NewResponseEntity(
                            HttpStatus.OK,
                            false,
                            "Failed to save book",
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            savedBook
                    );
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

                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.CREATED,
                        savedBook
                );
            } else {
                log.info("Book from database: {}", bookFromDatabase.getTitle());
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        false,
                        "Book already exists",
                        HttpStatus.CONFLICT,
                        savedBook
                );
            }
        } catch (Exception e) {
            log.error("Error saving book: {}", e.getMessage());
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    false,
                    "An unknown error occurred",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    savedBook
            );
        }
    }

    public ResponseEntity<Optional<Book>> deleteBookById(Long bookId) {
        Optional<Book> book = Optional.empty();
        try {
            book = bookRepository.findById(bookId);
            if (book.isPresent()) {
                bookRepository.delete(book.get());
                log.info("Deleted book with id: {}", bookId);

                bookEventPublisher.publishBookDeletedEvent(bookId);
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        book
                );
            } else {
                log.warn("Book with id {} not found for deletion", bookId);
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        false,
                        "Book not found for deletion",
                        HttpStatus.NOT_FOUND,
                        book
                );
            }
        } catch (Exception e) {
            log.error("Error deleting book with id {}: {}", bookId, e.getMessage());
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    false,
                    "An unknown error occurred",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    book
            );
        }
    }

    public ResponseEntity<Optional<Book>> fetchById(long id) {
        Optional<Book> book = Optional.empty();
        try {
            book = bookRepository.findById(id);
            if (book.isPresent()) {
                log.info("Book found with id: {}", id);
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        book
                );
            } else {
                log.warn("Book with id {} not found", id);
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        false,
                        "Book not found",
                        HttpStatus.NOT_FOUND,
                        book
                );
            }
        } catch (Exception exception) {
            log.error("Error fetching book by id {}: {}", id, exception.getMessage());
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    false,
                    "An unknown error occurred",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    book
            );

        }
    }

    public ResponseEntity<Optional<List<Book>>> fetchBooksByTitle(String title) {
        Optional<List<Book>> books = Optional.empty();
        try {
            books = Optional.ofNullable(bookRepository.findByTitle(title));
            if (books.isPresent()) {
                log.info("Found {} books with title containing: {}", books.get().size(), title);
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        books
                );
            } else {
                log.warn("No books found with title containing: {}", title);
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        false,
                        "No books found with the specified title",
                        HttpStatus.NOT_FOUND,
                        books
                );
            }
        } catch (Exception e) {
            log.error("Error fetching books by title {}: {}", title, e.getMessage());
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    false,
                    "An unknown error occurred",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    books
            );

        }
    }
}