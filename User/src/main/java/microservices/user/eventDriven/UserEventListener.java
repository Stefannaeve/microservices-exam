package microservices.user.eventDriven;

import lombok.extern.slf4j.Slf4j;
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
            Thread.sleep(5000);
            if ("DELETE".equals(userEvent.getEventType())) {
                handleUserDeletion(userEvent.getUserId());
            }
            else if ("DELETE_BOOK".equals(userEvent.getEventType())) {
                handleBookDeletion(userEvent.getUserId(), userEvent.getBookId());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
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

}
