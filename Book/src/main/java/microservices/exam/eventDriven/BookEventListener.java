package microservices.exam.eventDriven;

import lombok.extern.slf4j.Slf4j;
import microservices.exam.repository.BookRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

@Slf4j
@Component
public class BookEventListener {

    private final BookRepository bookRepository;
    private final Executor taskExecutor;

    public BookEventListener(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
        this.taskExecutor = createTaskExecutor();
    }

    private Executor createTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("book-listener-");
        executor.initialize();
        return executor;
    }

    @RabbitListener(queues = "${amqp.queue.book}")
    public void handleBookEvent(BookEvent bookEvent) {
        log.info("Received book event: {}", bookEvent);

        taskExecutor.execute(() -> {
            try {
                log.info("Processing event type: {}", bookEvent.getEventType());
                switch (bookEvent.getEventType()) {
                    case "CREATE" -> handleBookCreation(bookEvent);
                    case "DELETE" -> handleBookDeletion(bookEvent.getBookId());
                    default -> log.warn("Unknown event type: {}", bookEvent.getEventType());
                }
            } catch (Exception e) {
                log.error("Error processing book event: {}", e.getMessage(), e);
            }
        });
    }

    private void handleBookCreation(BookEvent bookEvent) {
        log.info("Handling book creation for bookId: {}, title: {}", bookEvent.getBookId(), bookEvent.getTitle());
    }

    private void handleBookDeletion(Long bookId) {
        bookRepository.findById(bookId).ifPresentOrElse(book -> {
            bookRepository.delete(book);
            log.info("Deleted book with id: {}", bookId);
        }, () -> log.warn("Book with id {} not found for deletion", bookId));
    }
}
