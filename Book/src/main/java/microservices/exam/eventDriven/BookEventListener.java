package microservices.exam.eventDriven;

import lombok.extern.slf4j.Slf4j;
import microservices.exam.models.Book;
import microservices.exam.repository.BookRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class BookEventListener {

    private final BookRepository bookRepository;

    @Autowired
    public BookEventListener(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @RabbitListener(queues = "${amqp.queue.book}")
    public void handleBookEvent(BookEvent bookEvent) {
        log.info("Received book event: {}", bookEvent);

        try {
            Thread.sleep(5000); // Simulating processing delay for testing
            String eventType = bookEvent.getEventType();
            log.info("Processing event type: {}", eventType);

            switch (eventType) {
                case "CREATE" -> handleBookCreation(bookEvent);
                case "DELETE" -> handleBookDeletion(bookEvent.getBookId());
                case "FETCH_TITLE" -> handleBookFetchByTitle(bookEvent.getTitle());
                default -> log.warn("Unknown event type: {}", eventType);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Error processing book event: {}", e.getMessage(), e);
        }
    }

    private void handleBookCreation(BookEvent bookEvent) {
        log.info("Handling book creation for bookId: {}, title: {}, author: {}",
                bookEvent.getBookId(), bookEvent.getTitle(), bookEvent.getAuthor());
    }

    private void handleBookDeletion(Long bookId) {
        log.info("Handling book deletion for bookId: {}", bookId);
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isPresent()) {
            bookRepository.delete(book.get());
            log.info("Deleted book with id: {}", bookId);
        } else {
            log.warn("Book with id {} not found for deletion", bookId);
        }
    }

    private void handleBookFetchByTitle(String title) {
        log.info("Handling book fetch for title: {}", title);
        List<Book> books = bookRepository.findByTitle(title);
        if (!books.isEmpty()) {
            log.info("Found {} books with title containing: {}", books.size(), title);
            books.forEach(book -> log.info("Book ID: {}, Title: {}, Author: {}", book.getId(), book.getTitle(), book.getAuthor()));
        } else {
            log.warn("No books found with title containing: {}", title);
        }
    }
}
