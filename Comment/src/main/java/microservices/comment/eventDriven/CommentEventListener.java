package microservices.comment.eventDriven;

import lombok.extern.slf4j.Slf4j;
import microservices.comment.models.Comment;
import microservices.comment.repository.CommentRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class CommentEventListener {

    private final CommentRepository commentRepository;

    public CommentEventListener(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @RabbitListener(queues = "${amqp.queue.comment}", concurrency = "3")
    public void handleCommentEvent(CommentEvent commentEvent) {
        log.info("Received comment event: {}", commentEvent);

        try {
            Thread.sleep(5000); // Simulate long processing time
            log.info("Processing event type: {}", commentEvent.getEventType());
            switch (commentEvent.getEventType()) {
                case "CREATE" -> handleCommentCreation(commentEvent);
                case "DELETE" -> handleCommentDeletion(commentEvent.getId());
                default -> log.warn("Unknown event type: {}", commentEvent.getEventType());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Error processing comment event: {}", e.getMessage(), e);
        }
    }

    private void handleCommentCreation(CommentEvent commentEvent) {
        log.info("Handling comment creation for commentId: {}, userId: {}, bookId: {}",
                commentEvent.getId(), commentEvent.getUserId(), commentEvent.getBookId());
    }

    private void handleCommentDeletion(Long commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        comment.ifPresentOrElse(
                c -> {
                    commentRepository.delete(c);
                    log.info("Deleted comment with id: {}", commentId);
                },
                () -> log.warn("Comment with id {} not found for deletion", commentId)
        );
    }
}
