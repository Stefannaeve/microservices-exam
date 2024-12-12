package microservices.exam.service;

import lombok.extern.slf4j.Slf4j;
import microservices.exam.apiResponse.ApiResponse;
import microservices.exam.apiResponse.ApiResponseBuilder;
import microservices.exam.eventDriven.BookEventPublisher;
import microservices.exam.models.Book;
import microservices.exam.repository.BookRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public ApiResponse<List<Book>> fetchAll() {
        ApiResponseBuilder<List<Book>> apiResponseBuilder = new ApiResponseBuilder<>();
        try {
            List<Book> books = bookRepository.findAll();
            log.info("Found {} books", books.size());
            return apiResponseBuilder.success(books);
        } catch (Exception exception) {
            log.error("Error fetching books: {}", exception.getMessage());
            return apiResponseBuilder.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong...");
        }
    }

    public ApiResponse<Book> saveOneBook(Book book) {
        ApiResponseBuilder<Book> apiResponseBuilder = new ApiResponseBuilder<>();
        try {
            Book savedBook = bookRepository.save(book);
            log.info("Saved book with id: {}", savedBook.getId());

            bookEventPublisher.publishBookCreatedEvent(
                    savedBook.getId(),
                    savedBook.getTitle(),
                    savedBook.getAuthor(),
                    savedBook.getPages(),
                    savedBook.getPublishDate(),
                    savedBook.getBookContent()
            );

            return apiResponseBuilder.success(savedBook);
        } catch (Exception e) {
            log.error("Error saving book: {}", e.getMessage());
            return apiResponseBuilder.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save book");
        }
    }

    public ApiResponse<Book> fetchById(long id) {
        ApiResponseBuilder<Book> apiResponseBuilder = new ApiResponseBuilder<>();
        try {
            Optional<Book> book = bookRepository.findById(id);
            if (book.isPresent()) {
                log.info("Book found with id: {}", id);
                return apiResponseBuilder.success(book.get());
            } else {
                log.warn("Book with id {} not found", id);
                return apiResponseBuilder.failure(HttpStatus.NOT_FOUND, "Book not found");
            }
        } catch (Exception exception) {
            log.error("Error fetching book by id {}: {}", id, exception.getMessage());
            return apiResponseBuilder.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong...");
        }
    }

    public ApiResponse<Void> deleteBookById(Long bookId) {
        ApiResponseBuilder<Void> apiResponseBuilder = new ApiResponseBuilder<>();
        try {
            Optional<Book> bookOptional = bookRepository.findById(bookId);
            if (bookOptional.isPresent()) {
                bookRepository.delete(bookOptional.get());
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

    public ApiResponse<List<Book>> fetchBooksByTitle(String title) {
        ApiResponseBuilder<List<Book>> apiResponseBuilder = new ApiResponseBuilder<>();

        try {
            bookEventPublisher.publishBookFetchEvent(title);
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