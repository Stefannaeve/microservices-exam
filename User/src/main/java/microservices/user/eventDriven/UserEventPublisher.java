package microservices.user.eventDriven;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;

    public UserEventPublisher(
            RabbitTemplate rabbitTemplate,
            @Value("${amqp.exchange.name}")
            String exchangeName){
        this.rabbitTemplate = rabbitTemplate;
        this.exchangeName = exchangeName;
    }

    public void publishDeleteEvent(Long userId) {
        UserEvent userEvent = new UserEvent(userId, "DELETE");
        rabbitTemplate.convertAndSend(exchangeName, "", userEvent);
        log.info("Published delete event for userId: {}", userId);
    }

    public void publishCreateEvent(UserEvent userEvent) {
        rabbitTemplate.convertAndSend(exchangeName, "", userEvent);
        log.info("Published create event for userId: {}", userEvent.getUserId());
    }

    public void publishBookDeletionEvent(Long userId, Long bookId) {
        UserEvent userEvent = new UserEvent(userId, "DELETE_BOOK", bookId);
        rabbitTemplate.convertAndSend(exchangeName, "", userEvent);
        log.info("Published book deletion event for userId: {} and bookId: {}", userId, bookId);
    }

}
