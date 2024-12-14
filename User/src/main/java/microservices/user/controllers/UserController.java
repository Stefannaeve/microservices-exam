package microservices.user.controllers;

import lombok.extern.slf4j.Slf4j;
import microservices.user.apiResponse.ApiResponse;
import microservices.user.models.User;
import microservices.user.models.UserBook;
import microservices.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/fetchById/{userId}")
    public ResponseEntity<ApiResponse<User>> fetchById(@PathVariable Long userId) {
        ApiResponse<User> apiResponse = userService.fetchUserById(userId);

        switch (apiResponse) {
            case ApiResponse.Success<User> success -> {
                if (success.value().isPresent()) {
                    log.info("Fetched user with id: {}", success.value().get().getId());
                } else {
                    log.info("Fetched user successfully, but no user data found.");
                }
                return ResponseEntity.status(HttpStatus.OK).body(success);
            }
            case ApiResponse.Failure<User> failure -> {
                log.error("Failed to fetch user with id: {}. Error: {}", userId, failure.errorMessage());
                return new ResponseEntity<>(failure, failure.status());
            }
        }
    }

    @PostMapping("/saveOneUser")
    public ResponseEntity<ApiResponse<User>> saveOneUser(@RequestBody User userToSave) {
        ApiResponse<User> savedOneUser = userService.saveOneUser(userToSave);

        switch (savedOneUser) {
            case ApiResponse.Success<User> success -> {
                if (success.value().isPresent()) {
                    log.info("Saved user with id: {}", success.value().get().getId());
                } else {
                    log.info("User saved successfully.");
                }
                return ResponseEntity.status(HttpStatus.CREATED).body(success);
            }
            case ApiResponse.Failure<User> failure -> {
                log.error("Failed to save user. Error: {}", failure.errorMessage());
                return new ResponseEntity<>(failure, failure.status());
            }
        }
    }

    @GetMapping("/fetchUserBooks/{userId}")
    public ResponseEntity<ApiResponse<List<Long>>> fetchUserBooks(@PathVariable Long userId) {
        ApiResponse<List<Long>> apiResponse = userService.fetchUserBooks(userId);

        switch (apiResponse) {
            case ApiResponse.Success<List<Long>> success -> {
                if (success.value().isPresent()) {
                    log.info("Fetched books for userId: {}. Total books: {}", userId, success.value().get().size());
                } else {
                    log.info("Fetched books for userId: {}, but no books found.", userId);
                }
                return ResponseEntity.status(HttpStatus.OK).body(success);
            }
            case ApiResponse.Failure<List<Long>> failure -> {
                log.error("Failed to fetch books for userId: {}. Error: {}", userId, failure.errorMessage());
                return new ResponseEntity<>(failure, failure.status());
            }
        }
    }

    @PostMapping("/addBookToUser/{userId}")
    public ResponseEntity<ApiResponse<User>> addBookToUser(@PathVariable Long userId, @RequestBody UserBook userBook) {
        ApiResponse<User> apiResponse = userService.addBookToUser(userId, userBook);

        switch (apiResponse) {
            case ApiResponse.Success<User> success -> {
                log.info("Successfully added book to user with id: {}", userId);
                return ResponseEntity.status(HttpStatus.OK).body(success);
            }
            case ApiResponse.Failure<User> failure -> {
                log.error("Failed to add book to user with id: {}. Error: {}", userId, failure.errorMessage());
                return new ResponseEntity<>(failure, failure.status());
            }
        }
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ApiResponse<User>> deleteUser(@PathVariable Long userId) {
        ApiResponse<User> apiResponse = userService.deleteUserById(userId);

        switch (apiResponse) {
            case ApiResponse.Success<User> success -> {
                log.info("Deleted user with id: {}", userId);
                return ResponseEntity.status(HttpStatus.OK).body(success);
            }
            case ApiResponse.Failure<User> failure -> {
                log.error("Failed to delete user with id: {}. Error: {}", userId, failure.errorMessage());
                return new ResponseEntity<>(failure, failure.status());
            }
        }
    }

    @DeleteMapping("/{userId}/deleteBook/{bookId}")
    public ResponseEntity<ApiResponse<User>> deleteBookFromUser(@PathVariable Long userId, @PathVariable Long bookId) {
        ApiResponse<User> apiResponse = userService.deleteBookFromUser(userId, bookId);

        switch (apiResponse) {
            case ApiResponse.Success<User> success -> {
                log.info("Deleted book with id: {} from user with id: {}", bookId, userId);
                return ResponseEntity.status(HttpStatus.OK).body(success);
            }
            case ApiResponse.Failure<User> failure -> {
                log.error("Failed to delete book with id: {} from user with id: {}. Error: {}", bookId, userId, failure.errorMessage());
                return new ResponseEntity<>(failure, failure.status());
            }
        }
    }

    @PatchMapping("/{userId}/books/{bookId}/progress")
    public ResponseEntity<ApiResponse<User>> updateReadingProgress(@PathVariable Long userId, @PathVariable Long bookId, @RequestBody Map<String, String> requestBody) {
        String newReadingProgress = requestBody.get("newReadingProgress");
        String newReadingStatus = requestBody.get("newReadingStatus");
        ApiResponse<User> apiResponse = userService.updateReadingProgress(userId, bookId, newReadingProgress, newReadingStatus);

        switch (apiResponse) {
            case ApiResponse.Success<User> success -> {
                log.info("Updated reading progress for userId: {} and bookId: {}. New progress: {}, New status: {}", userId, bookId, newReadingProgress, newReadingStatus);
                return ResponseEntity.status(HttpStatus.OK).body(success);
            }
            case ApiResponse.Failure<User> failure -> {
                log.error("Failed to update reading progress for userId: {} and bookId: {}. Error: {}", userId, bookId, failure.errorMessage());
                return new ResponseEntity<>(failure, failure.status());
            }
        }
    }

    @GetMapping("/fetchUserWithBook/{userId}/{bookId}")
    public ResponseEntity<ApiResponse<User>> fetchUserWithBook(@PathVariable Long userId, @PathVariable Long bookId){
       ApiResponse<User> user = userService.fetchUserWithBook(userId, bookId);

       switch (user) {
           case ApiResponse.Success<User> success -> {
               return ResponseEntity.status(HttpStatus.OK).body(success);
           }
           case ApiResponse.Failure<User> failure ->  {
               System.out.println("Something went wrong: " + failure.errorMessage());
               return new ResponseEntity<>(failure, failure.status());
           }
       }
    }

    @GetMapping("/{userId}/notFinishedReading")
    public ResponseEntity<ApiResponse<List<UserBook>>> fetchUnfinishedBooks(@PathVariable Long userId) {
        ApiResponse<List<UserBook>> apiResponse = userService.fetchNotFinishedBooks(userId);

        switch (apiResponse) {
            case ApiResponse.Success<List<UserBook>> success -> {
                log.info("Fetched unfinished books for userId: {}", userId);
                return ResponseEntity.status(HttpStatus.OK).body(success);
            }
            case ApiResponse.Failure<List<UserBook>> failure -> {
                log.error("Failed to fetch unfinished books for userId: {}. Error: {}", userId, failure.errorMessage());
                return new ResponseEntity<>(failure, failure.status());
            }
        }
    }
}
