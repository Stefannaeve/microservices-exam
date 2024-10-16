package eventDriven;

import lombok.extern.slf4j.Slf4j;
import microservices.comment.service.CommentService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CommentEventListener {

    private final CommentService commentService;

    public CommentEventListener(CommentService commentService) {
        this.commentService = commentService;
    }

    @RabbitListener(queues = "${amqp.queue.comment}")
    public void handleBookDeletedEvent(Long bookId) {
        log.info("Received 'book.deleted' event for bookId: {}", bookId);

        // Delete comments associated with this book
        commentService.deleteCommentsByBookId(bookId);
        log.info("Deleted comments associated with bookId: {}", bookId);
    }
}
