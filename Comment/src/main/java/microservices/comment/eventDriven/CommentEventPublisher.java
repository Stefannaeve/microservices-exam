package microservices.comment.eventDriven;

import lombok.extern.slf4j.Slf4j;
import microservices.comment.models.Comment;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CommentEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;

    public CommentEventPublisher(RabbitTemplate rabbitTemplate, @Value("${amqp.exchange.comment}") String exchangeName) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchangeName = exchangeName;
    }

    public void publishCommentCreatedEvent(Long commentId, Long userId, Long bookId) {
        log.info("Preparing to publish comment created event: commentId={}, userId={}, bookId={}", commentId, userId, bookId);
        try {
            CommentEvent commentEvent = new CommentEvent(commentId, "CREATE", userId, bookId);
            rabbitTemplate.convertAndSend(exchangeName, "", commentEvent);
            log.info("Successfully published comment created event to exchange '{}'", exchangeName);
        } catch (Exception e) {
            log.error("Failed to publish comment created event: {}", e.getMessage(), e);
        }
    }

    public void publishCommentDeletedEvent(Long commentId) {
        log.info("Preparing to publish comment deleted event: commentId={}", commentId);
        try {
            CommentEvent commentEvent = new CommentEvent(commentId, "DELETE");
            rabbitTemplate.convertAndSend(exchangeName, "", commentEvent);
            log.info("Successfully published comment deleted event to exchange '{}'", exchangeName);
        } catch (Exception e) {
            log.error("Failed to publish comment deleted event: {}", e.getMessage(), e);
        }
    }

    public void publishCommentUpdatedEvent(Long commentId, String newText) {
        log.info("Preparing to publish comment updated event: commentId={}, newText={}", commentId, newText);
        try {
            CommentEvent commentEvent = new CommentEvent(commentId, "UPDATE", newText);
            rabbitTemplate.convertAndSend(exchangeName, "", commentEvent);
            log.info("Successfully published comment updated event to exchange '{}'", exchangeName);
        } catch (Exception e) {
            log.error("Failed to publish comment updated event: {}", e.getMessage(), e);
        }
    }
}
