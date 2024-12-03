package microservices.user.services;

import lombok.extern.slf4j.Slf4j;
import microservices.user.eventDriven.UserEvent;
import microservices.user.eventDriven.UserEventPublisher;
import microservices.user.models.User;
import microservices.user.models.UserBook;
import microservices.user.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    private final UserRepo userRepo;
    private final UserEventPublisher userEventPublisher;


    @Autowired
    public UserService(UserRepo userRepo, UserEventPublisher userEventPublisher) {
        this.userRepo = userRepo;
        this.userEventPublisher = userEventPublisher
        ;
    }

    public ResponseEntity saveOneUser(User userToSave){
        try {
            User savedUser = userRepo.save(userToSave);
            UserEvent createEvent = new UserEvent(savedUser.getId(), "CREATE");
            userEventPublisher.publishCreateEvent(createEvent);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (Exception e) {
            log.error("Error saving user: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public ResponseEntity fetchUserById(Long id){
        User user = userRepo.findById(id).orElse(null);
        if (user != null){
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Error message", "No matching user found").body("No matching user");
    }

    public ResponseEntity fetchUserBooks(Long userId){
        User user = userRepo.findById(userId).orElse(null);
        if (user == null){
            return ResponseEntity.status(
                    HttpStatus.NOT_FOUND).header("Error message", "No matching user found").body("No user found");
        }
        if(user.getBooks() != null){
            List<Long> bookIdList = new ArrayList<>();
            user.getBooks().forEach(bookId -> bookIdList.add(bookId.getId()));
            return ResponseEntity.status(HttpStatus.OK).body(bookIdList);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Error " +
                "message", "User has no books").body("User has no books");
    }

    public ResponseEntity addBookToUser(Long userId, UserBook userBook){
        User user = userRepo.findById(userId).orElse(null);
        if (user == null){
            log.info(String.valueOf(user.getId()));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Error message", "No matching user found")
                    .body("No user found");
        }
        user.getBooks().add(userBook);
        userRepo.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    public ResponseEntity deleteUserById(Long userId) {
        User user = userRepo.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        userEventPublisher.publishDeleteEvent(userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("User deletion request sent to queue");
    }

    public ResponseEntity deleteBookFromUser(Long userId, Long bookId) {
        User user = userRepo.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        Optional<UserBook> userBook = user.getBooks()
                .stream()
                .filter(book -> book.getId().equals(bookId))
                .findFirst();

        if (userBook.isPresent()) {
            user.getBooks().remove(userBook.get());
            userRepo.save(user);
            userEventPublisher.publishBookDeletionEvent(userId, bookId);
            return ResponseEntity.status(HttpStatus.OK).body("Removed book from user");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found in user's book list");
        }
    }
}
