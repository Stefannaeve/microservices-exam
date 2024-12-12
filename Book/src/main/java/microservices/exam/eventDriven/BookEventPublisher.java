package microservices.exam.eventDriven;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class BookEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;

    public BookEventPublisher(RabbitTemplate rabbitTemplate, @Value("${amqp.exchange.name}") String exchangeName) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchangeName = exchangeName;
    }

    public void publishBookCreatedEvent(Long bookId, String title, String author, int pages, Date publishDate, String bookContent) {
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

    public void publishBookFetchEvent(String title) {
        log.info("Preparing to publish book fetch event for title: {}", title);
        BookEvent bookEvent = new BookEvent(null, "FETCH_TITLE", title, null, 0, null, null);
        rabbitTemplate.convertAndSend(exchangeName, "", bookEvent);
        log.info("Published book fetch event for title: {}", title);
    }
}
