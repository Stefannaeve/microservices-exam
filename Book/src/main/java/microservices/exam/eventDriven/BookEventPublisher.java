package microservices.exam.eventDriven;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BookEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;

    public BookEventPublisher(RabbitTemplate rabbitTemplate, @Value("${amqp.exchange.name}") String exchangeName) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchangeName = exchangeName;
    }

    public void publishBookEvent(BookEvent bookEvent) {
        String routingKey = "book.comment.created"; // Customize routing key as needed
        rabbitTemplate.convertAndSend(exchangeName, routingKey, bookEvent);
        log.info("Published event to exchange '{}' with routing key '{}': {}", exchangeName, routingKey, bookEvent);
    }
}
