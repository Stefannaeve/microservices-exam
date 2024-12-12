
package microservices.user.eventDriven;

import lombok.extern.slf4j.Slf4j;
import microservices.user.models.ReadingStatus;
import microservices.user.models.User;
import microservices.user.models.UserBook;
import microservices.user.repositories.UserRepo;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class UserEventListener {

    private final UserRepo userRepo;

    public UserEventListener(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @RabbitListener(queues = "${amqp.queue.user}")
    public void handleUserEvent(UserEvent userEvent) {
        log.info("Received event: {}", userEvent);
        try {
            Thread.sleep(5000); // for testing purposes

            String eventType = userEvent.getEventType();
            if ("DELETE".equals(eventType)) {
                handleUserDeletion(userEvent.getUserId());
            } else if ("DELETE_BOOK".equals(eventType)) {
                handleBookDeletion(userEvent.getUserId(), userEvent.getBookId());
            } else if ("UPDATE_PROGRESS".equals(eventType)) {
                handleProgressUpdate(userEvent);
            } else if ("CREATE".equals(eventType)) {
                handleUserCreation(userEvent);
            } else {
                log.warn("Unknown event type: {}", eventType);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Error processing event: {}", e.getMessage(), e);
        }
    }

    private void handleUserCreation(UserEvent userEvent) {
        log.info("Handling user creation event for userId: {}, username: {}",
                userEvent.getUserId(), userEvent.getUsername());
    }



    private void handleUserDeletion(Long userId) {
        User user = userRepo.findById(userId).orElse(null);
        if (user != null) {
            userRepo.delete(user);
            log.info("Deleted user and books for userId: {}", userId);
        } else {
            log.warn("User with id {} not found during deletion", userId);
        }
    }

    private void handleBookDeletion(Long userId, Long bookId) {
        User user = userRepo.findById(userId).orElse(null);
        if (user != null) {
            Optional<UserBook> userBook = user.getBooks()
                    .stream()
                    .filter(book -> book.getId().equals(bookId))
                    .findFirst();
            if (userBook.isPresent()) {
                user.getBooks().remove(userBook.get());
                userRepo.save(user);
                log.info("Deleted book with id {} from userId: {}", bookId, userId);
            } else {
                log.warn("Book with id {} not found in user's collection", bookId);
            }
        } else {
            log.warn("User with id {} not found during book deletion", userId);
        }
    }

    private void handleProgressUpdate(UserEvent userEvent) {
        Long userId = userEvent.getUserId();
        Long bookId = userEvent.getBookId();
        String newProgress = userEvent.getReadingProgress();

        User user = userRepo.findById(userId).orElse(null);
        if (user == null) {
            log.warn("User with id {} not found while handling UPDATE_PROGRESS event", userId);
            return;
        }

        Optional<UserBook> optionalBook = user.getBooks()
                .stream()
                .filter(book -> book.getId().equals(bookId))
                .findFirst();

        if (optionalBook.isPresent()) {
            UserBook book = optionalBook.get();
            book.setReadingProgress(newProgress);

            if ("100%".equals(newProgress)) {
                book.setReadingStatus(ReadingStatus.Finished);
            }

            userRepo.save(user);
            log.info("Updated progress for userId: {}, bookId: {} to {}", userId, bookId, newProgress);
        } else {
            log.warn("Book with id {} not found in user {}'s collection", bookId, userId);
        }
    }
}
