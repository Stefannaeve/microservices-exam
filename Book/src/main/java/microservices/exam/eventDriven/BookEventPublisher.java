package microservices.exam.eventDriven;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Service
@Slf4j
public class BookEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;

    public BookEventPublisher(
            RabbitTemplate rabbitTemplate,
            @Value("${amqp.exchange.name}") String exchangeName
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchangeName = exchangeName;
    }

    public void publishCreatedBookEvent(BookEvent bookEvent) {
        String routingKey = "book.created";
        rabbitTemplate.convertAndSend(exchangeName, routingKey, bookEvent);
        log.info("Published 'book.created' event to exchange '{}' with routing key '{}': {}",
                exchangeName, routingKey, bookEvent);
    }

    public void publishDeletedBookEvent(Long bookId) {
        String routingKey = "book.deleted";
        rabbitTemplate.convertAndSend(exchangeName, routingKey, bookId);
        log.info("Published 'book.deleted' event for bookId: {}", bookId);
    }
}
