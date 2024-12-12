
package microservices.exam.eventDriven;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
public class BookEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;

    public BookEventPublisher(RabbitTemplate rabbitTemplate, @Value("${amqp.exchange.name}") String exchangeName) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchangeName = exchangeName;
    }

    public void publishBookCreatedEvent(Long bookId, String title, String author, int pages, LocalDate publishDate, String bookContent) {
        log.info("Preparing to publish book created event for bookId: {}", bookId);
        BookEvent bookEvent = new BookEvent(bookId, "CREATE", title, author, pages, publishDate, bookContent);
        rabbitTemplate.convertAndSend(exchangeName, "", bookEvent);
        log.info("Published book created event for bookId: {}", bookId);
    }

    public void publishBookDeletedEvent(Long bookId) {
        log.info("Preparing to publish book deleted event for bookId: {}", bookId);
        BookEvent bookEvent = new BookEvent(bookId, "DELETE");
        rabbitTemplate.convertAndSend(exchangeName, "", bookEvent);
        log.info("Published book deleted event for bookId: {}", bookId);
    }

    public void publishBookUpdatedEvent(Long bookId, String title, String author, int pages, LocalDate publishDate, String bookContent) {
        log.info("Preparing to publish book updated event for bookId: {}", bookId);
        BookEvent bookEvent = new BookEvent(bookId, "UPDATE", title, author, pages, publishDate, bookContent);
        rabbitTemplate.convertAndSend(exchangeName, "", bookEvent);
        log.info("Published book updated event for bookId: {}", bookId);
    }
}
