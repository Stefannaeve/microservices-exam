
package microservices.exam.eventDriven;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BookEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;

    public BookEventPublisher(
            RabbitTemplate rabbitTemplate,
            @Value("${amqp.exchange.name}") String exchangeName) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchangeName = exchangeName;
    }

    public void publishCreatedBookEvent(BookEvent bookEvent) {
        rabbitTemplate.convertAndSend(exchangeName, "", bookEvent);
        log.info("Published 'book.created' event to exchange '{}': {}", exchangeName, bookEvent);
    }

    public void publishDeletedBookEvent(Long bookId) {
        rabbitTemplate.convertAndSend(exchangeName, "", bookId);
        log.info("Published 'book.deleted' event for bookId: {}", bookId);
    }
}

