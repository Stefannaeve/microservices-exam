package microservices.comment.eventDriven;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservices.comment.models.Comment;
import microservices.comment.service.CommentService;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookEventHandler {

    private final CommentService commentService;

    @RabbitListener(queues = "comment.queue")
    public void handleBookEvent(final Comment comment){
        log.info("Message received: {}", comment);

        try {
            commentService.saveOneComment(comment);
        }catch(final Exception e){
            log.error("Error processing book. " + e.getMessage());
            throw new AmqpRejectAndDontRequeueException(e);
        }
        
    }

}
