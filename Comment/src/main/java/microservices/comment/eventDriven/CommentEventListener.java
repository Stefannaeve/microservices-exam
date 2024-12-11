package microservices.comment.eventDriven;

import lombok.extern.slf4j.Slf4j;
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
        String eventType = commentEvent.getEventType();

        switch (eventType) {
            case "CREATE" -> handleCommentCreation(commentEvent);
            case "DELETE" -> handleCommentDeletion(commentEvent.getId());
            case "UPDATE" -> handleCommentUpdate(commentEvent.getId(), commentEvent.getText());
            default -> log.warn("Unknown event type: {}", eventType);
        }
    }

    private void handleCommentCreation(CommentEvent commentEvent) {
        log.info("Handling comment creation for commentId: {}, userId: {}, bookId: {}",
                commentEvent.getId(), commentEvent.getUserId(), commentEvent.getBookId());
        // Additional logic if needed
    }

    private void handleCommentDeletion(Long commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isPresent()) {
            commentRepository.delete(comment.get());
            log.info("Deleted comment with id: {}", commentId);
        } else {
            log.warn("Comment with id {} not found for deletion", commentId);
        }
    }

    private void handleCommentUpdate(Long commentId, String newText) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isPresent()) {
            Comment existingComment = comment.get();
            existingComment.setText(newText);
            commentRepository.save(existingComment);
            log.info("Updated comment with id: {} to new text: {}", commentId, newText);
        } else {
            log.warn("Comment with id {} not found for update", commentId);
        }
    }
}
