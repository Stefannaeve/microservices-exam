package microservices.exam.eventDriven;

import lombok.extern.slf4j.Slf4j;
import microservices.exam.models.Book;
import microservices.exam.repository.BookRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        String eventType = bookEvent.getEventType();

        switch (eventType) {
            case "CREATE" -> handleBookCreation(bookEvent);
            case "DELETE" -> handleBookDeletion(bookEvent.getBookId());
            case "UPDATE" -> handleBookUpdate(bookEvent);
            default -> log.warn("Unknown event type: {}", eventType);
        }
    }

    private void handleBookCreation(BookEvent bookEvent) {
        log.info("Handling book creation for bookId: {}, title: {}, author: {}",
                bookEvent.getBookId(), bookEvent.getTitle(), bookEvent.getAuthor());
        // Additional logic if needed
    }

    private void handleBookDeletion(Long bookId) {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isPresent()) {
            bookRepository.delete(book.get());
            log.info("Deleted book with id: {}", bookId);
        } else {
            log.warn("Book with id {} not found for deletion", bookId);
        }
    }

    private void handleBookUpdate(BookEvent bookEvent) {
        Optional<Book> book = bookRepository.findById(bookEvent.getBookId());
        if (book.isPresent()) {
            Book existingBook = book.get();
            existingBook.setTitle(bookEvent.getTitle());
            existingBook.setAuthor(bookEvent.getAuthor());
            existingBook.setPages(bookEvent.getPages());
            existingBook.setPublishDate(bookEvent.getPublishDate());
            existingBook.setBookContent(bookEvent.getBookContent());
            bookRepository.save(existingBook);
            log.info("Updated book with id: {}", bookEvent.getBookId());
        } else {
            log.warn("Book with id {} not found for update", bookEvent.getBookId());
        }
    }
}
