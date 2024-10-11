package microservices.manager.eventDriven;

import lombok.extern.slf4j.Slf4j;
import microservices.manager.dtos.BookDTO;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ManagerEventPublisher {

    private final AmqpTemplate amqpTemplate;
    private final String exchangeName;

    public ManagerEventPublisher(
            final AmqpTemplate amqpTemplate,
            @Value("${amqp.exchange.name}")
            final String exchangeName
    ) {
        this.amqpTemplate = amqpTemplate;
        this.exchangeName = exchangeName;
    }

    public void publishBookEvent(BookDTO bookDTO) {
        String routingKey = "manager.book.created";
        amqpTemplate.convertAndSend(exchangeName, routingKey, bookDTO);
        log.info("Published Book Created Event: {}", bookDTO);
    }
}
