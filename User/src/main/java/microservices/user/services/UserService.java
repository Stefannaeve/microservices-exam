package microservices.user.services;

import lombok.extern.slf4j.Slf4j;
import microservices.user.apiResponse.ApiResponse;
import microservices.user.apiResponse.ApiResponseBuilder;
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

    public ApiResponse<User> saveOneUser(User userToSave){
        ApiResponseBuilder<User> apiResponseBuilder = new ApiResponseBuilder<>();
        try {
            User savedUser = userRepo.save(userToSave);
            UserEvent createEvent = new UserEvent(savedUser.getId(), "CREATE");
            userEventPublisher.publishCreateEvent(createEvent);
            return apiResponseBuilder.success(savedUser);
        } catch (Exception e) {
            log.error("Error saving user: {}", e.getMessage(), e);
            return new ApiResponse.Failure<>(Optional.empty(), HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save user");
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

    public ApiResponse<User> addBookToUser(Long userId, UserBook userBook){
        ApiResponseBuilder<User> apiResponseBuilder = new ApiResponseBuilder<>();

        try {
            User user = userRepo.findById(userId).orElse(null);
            if (user == null){
                log.info(String.valueOf(user.getId()));
                return apiResponseBuilder.failure(HttpStatus.NOT_FOUND, "No matching user found");
            }
            user.getBooks().add(userBook);
            userRepo.save(user);
            return apiResponseBuilder.success(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ApiResponse<User> deleteUserById(Long userId) {
        ApiResponseBuilder<User> apiResponseBuilder = new ApiResponseBuilder<>();
        try {
            User user = userRepo.findById(userId).orElse(null);
            if (user == null) {
                return apiResponseBuilder.failure(HttpStatus.NOT_FOUND, "User not found");
            }
            userEventPublisher.publishDeleteEvent(userId);
            return apiResponseBuilder.success(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ApiResponse<User> deleteBookFromUser(Long userId, Long bookId) {
        ApiResponseBuilder<User> apiResponseBuilder = new ApiResponseBuilder<>();
        try {
            User user = userRepo.findById(userId).orElse(null);
            if (user == null) {
                return apiResponseBuilder.failure(HttpStatus.NOT_FOUND, "User not found");
            }
            Optional<UserBook> userBook = user.getBooks()
                    .stream()
                    .filter(book -> book.getId().equals(bookId))
                    .findFirst();

            if (userBook.isPresent()) {
                user.getBooks().remove(userBook.get());
                userRepo.save(user);
                userEventPublisher.publishBookDeletionEvent(userId, bookId);
                return apiResponseBuilder.success(user);
            } else {
                return apiResponseBuilder.failure(HttpStatus.NOT_FOUND, "Book not found in user's book list");
            }
        } catch (Exception e) {
            return apiResponseBuilder.failure(HttpStatus.NOT_FOUND, "User has no books");
        }
    }
}
