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

    public void publishCommentCreatedEvent(Comment comment) {
        CommentEvent commentEvent = new CommentEvent(
                comment.getUserId(),
                comment.getBookId(),
                comment.getPage(),
                comment.isPositive(),
                comment.isNegative(),
                comment.getText()
        );
        rabbitTemplate.convertAndSend(exchangeName, "", commentEvent);
    }
}
