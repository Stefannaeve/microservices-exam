
package microservices.comment.eventDriven;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import microservices.comment.models.Comment;
import microservices.comment.repository.CommentRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

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
        log.info("Received comment event: {}", commentEvent);

        try {
            Thread.sleep(5000); // For testing purposes to imitate large message payloads

            String eventType = commentEvent.getEventType();
            log.info("Processing event type: {}", eventType);

            switch (eventType){
                case "CREATE" -> handleCommentCreation(commentEvent);
                case "DELETE" -> handleCommentDeletion(commentEvent.getId());
                default -> log.warn("Unknown event type: {}", eventType);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Error processing comment event: {}", e.getMessage(), e);
        }
    }

    private void handleCommentCreation(CommentEvent commentEvent) {
        log.info("Handling comment creation: commentId={}, userId={}, bookId={}",
                commentEvent.getId(), commentEvent.getUserId(), commentEvent.getBookId());
    }

    private void handleCommentDeletion(Long commentId) {
        log.info("Handling comment deletion for commentId: {}", commentId);
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isPresent()) {
            commentRepository.delete(comment.get());
            log.info("Deleted comment with id: {}", commentId);
        } else {
            log.warn("Comment with id {} not found for deletion", commentId);
        }
    }
}
