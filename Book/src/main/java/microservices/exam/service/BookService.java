package microservices.exam.service;

import lombok.extern.slf4j.Slf4j;
import microservices.exam.apiResponse.ApiResponse;
import microservices.exam.apiResponse.ApiResponseBuilder;
import microservices.exam.eventDriven.BookEvent;
import microservices.exam.eventDriven.BookEventPublisher;
import microservices.exam.models.Book;
import microservices.exam.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BookService {

    private final BookEventPublisher bookEventPublisher;
    private final BookRepository bookRepository;

    public BookService(BookEventPublisher bookEventPublisher, BookRepository bookRepository) {
        this.bookEventPublisher = bookEventPublisher;
        this.bookRepository = bookRepository;
    }


    public ApiResponse<List<Book>> fetchAll() {
        ApiResponseBuilder<List<Book>> apiResponseBuilder = new ApiResponseBuilder<>();
        try {
            List<Book> listBooks = bookRepository.findAll();
            log.info("Found {} books", listBooks.size());
            return apiResponseBuilder.success(listBooks);
        } catch (Exception exception) {
            log.error("Error fetching books: {}", exception.getMessage());
            return apiResponseBuilder.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong while fetching books.");
        }
    }

    public ApiResponse<Book> saveOneBook(Book book) {
        ApiResponseBuilder<Book> apiResponseBuilder = new ApiResponseBuilder<>();
        try {
            Book savedBook = bookRepository.save(book);
            log.info("Book saved with id: {}", savedBook.getId());

            // Publish event after saving book
            BookEvent bookEvent = new BookEvent(savedBook.getId(), savedBook.getTitle(), savedBook.getAuthor(),
                    savedBook.getPages(), savedBook.getPublishDate(), savedBook.getBookContent());
            bookEventPublisher.publishCreatedBookEvent(bookEvent);

            return apiResponseBuilder.success(savedBook);
        } catch (Exception exception) {
            log.error("Error saving book: {}", exception.getMessage());
            return apiResponseBuilder.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save book.");
        }
    }

    public ApiResponse<Void> deleteBook(Long bookId) {
        ApiResponseBuilder<Void> apiResponseBuilder = new ApiResponseBuilder<>();
        try {
            bookRepository.deleteById(bookId);
            log.info("Book deleted with id: {}", bookId);

            // Publish event after deleting book
            bookEventPublisher.publishDeletedBookEvent(bookId);

            return apiResponseBuilder.success(null);
        } catch (Exception exception) {
            log.error("Error deleting book: {}", exception.getMessage());
            return apiResponseBuilder.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete book.");
        }
    }


    private void publishBookEvent(Book savedBook) {
        BookEvent bookEvent = new BookEvent(
                savedBook.getId(),
                savedBook.getTitle(),
                savedBook.getAuthor(),
                savedBook.getPages(),
                savedBook.getPublishDate(),
                savedBook.getBookContent()
        );
        try {
            bookEventPublisher.publishCreatedBookEvent(bookEvent);
            log.info("Book creation event published for book id: {}", savedBook.getId());
        } catch (Exception eventException) {
            log.error("Error publishing book event: {}", eventException.getMessage());
            throw new RuntimeException("Book saved, but failed to publish event.");
        }
    }
}
