package microservices.comment.eventDriven;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservices.comment.models.Comment;
import microservices.comment.service.CommentService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookEventHandler {

    private final CommentService commentService;

    @RabbitListener(queues = "${amqp.queue.name}")
    public void handleBookEvent(Comment message){
        log.info("Message received: {}", message);
            commentService.saveOneComment(message);
    }
}
