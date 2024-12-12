
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

    public void publishCreateEvent(Long userId, String username) {
        UserEvent createEvent = new UserEvent(userId, "CREATE", username);
        rabbitTemplate.convertAndSend(exchangeName, "", createEvent);
        log.info("Published create event for userId: {}", userId);
    }


    public void publishBookDeletionEvent(Long userId, Long bookId) {
        UserEvent userEvent = new UserEvent(userId, "DELETE_BOOK", bookId);
        rabbitTemplate.convertAndSend(exchangeName, "", userEvent);
        log.info("Published book deletion event for userId: {} and bookId: {}", userId, bookId);
    }

    public void publishProgressUpdateEvent(Long userId, Long bookId, String newProgress, String newStatus) {
        UserEvent userEvent = new UserEvent(userId, "UPDATE_PROGRESS", bookId, newProgress, newStatus);
        rabbitTemplate.convertAndSend(exchangeName, "", userEvent);
        log.info("Published progress update event for userId: {}, bookId: {}, newProgress: {}, newStatus: {}", userId, bookId, newProgress, newStatus);
    }
}
