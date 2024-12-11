package microservices.exam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservices.exam.apiResponse.ApiResponse;
import microservices.exam.apiResponse.ApiResponseBuilder;
import microservices.exam.eventDriven.BookEventPublisher;
import microservices.exam.models.Book;
import microservices.exam.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookEventPublisher bookEventPublisher;

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
                    savedBook.getBookContent()
            );

            return apiResponseBuilder.success(savedBook);
        } catch (Exception e) {
            log.error("Error saving book: {}", e.getMessage());
            return apiResponseBuilder.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save book");
        }
    }

    public ApiResponse<Void> deleteBookById(Long bookId) {
        ApiResponseBuilder<Void> apiResponseBuilder = new ApiResponseBuilder<>();
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
}
