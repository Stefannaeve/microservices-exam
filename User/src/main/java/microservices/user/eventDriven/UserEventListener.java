
package microservices.user.eventDriven;

import lombok.extern.slf4j.Slf4j;
import microservices.user.models.ReadingStatus;
import microservices.user.models.User;
import microservices.user.models.UserBook;
import microservices.user.repositories.UserBookRepo;
import microservices.user.repositories.UserRepo;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class UserEventListener {

    private final UserRepo userRepo;
    private final UserBookRepo userBookRepo;

    public UserEventListener(UserRepo userRepo, UserBookRepo userBookRepo) {
        this.userRepo = userRepo;
        this.userBookRepo = userBookRepo;
    }

    @RabbitListener(queues = "${amqp.queue.user}")
    public void handleUserEvent(UserEvent userEvent) {
        log.info("Received event: {}", userEvent);

        try {
            Thread.sleep(5000); // For testing purposes to imitate large message payloads
            String eventType = userEvent.getEventType();

            switch (eventType) {
                case "DELETE" -> handleUserDeletion(userEvent.getUserId());
                case "DELETE_BOOK" -> handleBookDeletion(userEvent.getUserId(), userEvent.getBookId());
                case "ADD_BOOK" -> handleAddBook(userEvent.getUserId(), userEvent.getUserBook());
                case "CREATE" -> handleUserCreation(userEvent);
                default -> log.warn("Unknown event type: {}", eventType);
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

    private void handleAddBook(Long userId, UserBook userBook) {
        User user = userRepo.findById(userId).orElse(null);
        if (user != null) {
            UserBook managedBook = userBookRepo.findById(userBook.getId()).orElse(userBook);

            if (user.getBooks().stream().noneMatch(book -> book.getId().equals(managedBook.getId()))) {
                user.getBooks().add(managedBook);
                userRepo.save(user);
                log.info("Added book with id {} to userId: {}", managedBook.getId(), userId);
            } else {
                log.warn("Book with id {} already exists for userId: {}", managedBook.getId(), userId);
            }
        } else {
            log.warn("User with id {} not found during book addition", userId);
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
