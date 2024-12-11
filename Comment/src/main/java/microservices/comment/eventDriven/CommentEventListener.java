package microservices.comment.eventDriven;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import microservices.comment.models.Comment;
import microservices.comment.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Component
public class CommentEventListener {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentEventListener(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @RabbitListener(queues = "${amqp.queue.comment}")
    public void handleCommentEvent(CommentEvent commentEvent) {
    }
}
