package microservices.exam.service;

import lombok.extern.slf4j.Slf4j;
import microservices.exam.apiResponse.ApiResponse;
import microservices.exam.apiResponse.ApiResponseBuilder;
import microservices.exam.eventDriven.BookEvent;
import microservices.exam.eventDriven.BookEventPublisher;
import microservices.exam.models.Book;
import microservices.exam.repository.BookRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BookService {

    BookRepository bookRepository;
    BookEventPublisher bookEventPublisher;

    public BookService(BookRepository bookRepository, BookEventPublisher bookEventPublisher) {
        this.bookRepository = bookRepository;
        this.bookEventPublisher = bookEventPublisher;
    }

    public ApiResponse<List<Book>> fetchAll() {
        ApiResponseBuilder<List<Book>> apiResponseBuilder = new ApiResponseBuilder<>();
        List<Book> listBooks;

        try {
            listBooks = bookRepository.findAll();
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return apiResponseBuilder.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong...");
        }

        log.info("Found {} books", listBooks.size());
        return apiResponseBuilder.success(listBooks);
    }

    public ApiResponse<Book> saveOneBook(Book book) {
        ApiResponseBuilder<Book> apiResponseBuilder = new ApiResponseBuilder<>();
        try {
            // Save the book and publish an event if successful
            Book savedBook = bookRepository.save(book);
            log.info("Added book with id: {} to the database", savedBook.getId());

            // Publish an event indicating the book has been created
            bookEventPublisher.publishCreatedBookEvent(new BookEvent(
                    savedBook.getId(),
                    savedBook.getTitle(),
                    savedBook.getAuthor(),
                    savedBook.getPages(),
                    savedBook.getPublishDate(),
                    savedBook.getBookContent()
            ));
            return apiResponseBuilder.success(savedBook);
        }
        catch (Exception e){
            return new ApiResponse.Failure<>(Optional.empty(), HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save book");
        }
    }
}
