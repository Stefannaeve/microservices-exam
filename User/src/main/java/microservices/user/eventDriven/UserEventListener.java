package microservices.user.eventDriven;

import lombok.extern.slf4j.Slf4j;
import microservices.user.models.User;
import microservices.user.models.UserBook;
import microservices.user.repositories.UserBookRepo;
import microservices.user.repositories.UserRepo;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.Executor;

@Component
@Slf4j
public class UserEventListener {

    private final UserRepo userRepo;
    private final UserBookRepo userBookRepo;
    private final Executor taskExecutor;

    public UserEventListener(UserRepo userRepo, UserBookRepo userBookRepo) {
        this.userRepo = userRepo;
        this.userBookRepo = userBookRepo;
        this.taskExecutor = createTaskExecutor();
    }

    private Executor createTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("user-listener-");
        executor.initialize();
        return executor;
    }

    @RabbitListener(queues = "${amqp.queue.user}")
    public void handleUserEvent(UserEvent userEvent) {
        log.info("Received event: {}", userEvent);

        taskExecutor.execute(() -> {
            try {
                log.info("Processing event type: {}", userEvent.getEventType());
                switch (userEvent.getEventType()) {
                    case "DELETE" -> handleUserDeletion(userEvent.getUserId());
                    case "DELETE_BOOK" -> handleBookDeletion(userEvent.getUserId(), userEvent.getBookId());
                    case "ADD_BOOK" -> handleAddBook(userEvent.getUserId(), userEvent.getUserBook());
                    case "CREATE" -> handleUserCreation(userEvent);
                    default -> log.warn("Unknown event type: {}", userEvent.getEventType());
                }
            } catch (Exception e) {
                log.error("Error processing event: {}", e.getMessage(), e);
            }
        });
    }

    private void handleUserCreation(UserEvent userEvent) {
        log.info("Handling user creation event for userId: {}, username: {}", userEvent.getUserId(), userEvent.getUsername());
    }

    private void handleUserDeletion(Long userId) {
        userRepo.findById(userId).ifPresentOrElse(user -> {
            userRepo.delete(user);
            log.info("Deleted user with userId: {}", userId);
        }, () -> log.warn("User with id {} not found during deletion", userId));
    }

    private void handleAddBook(Long userId, UserBook userBook) {
        userRepo.findById(userId).ifPresentOrElse(user -> {
            UserBook managedBook = userBookRepo.findById(userBook.getId()).orElse(userBook);
            if (user.getBooks().stream().noneMatch(book -> book.getId().equals(managedBook.getId()))) {
                user.getBooks().add(managedBook);
                userRepo.save(user);
                log.info("Added book with id {} to userId: {}", managedBook.getId(), userId);
            } else {
                log.warn("Book with id {} already exists for userId: {}", managedBook.getId(), userId);
            }
        }, () -> log.warn("User with id {} not found during book addition", userId));
    }

    private void handleBookDeletion(Long userId, Long bookId) {
        userRepo.findById(userId).ifPresentOrElse(user -> {
            user.getBooks().removeIf(book -> book.getId().equals(bookId));
            userRepo.save(user);
            log.info("Deleted book with id {} from userId: {}", bookId, userId);
        }, () -> log.warn("User with id {} not found during book deletion", userId));
    }
}
