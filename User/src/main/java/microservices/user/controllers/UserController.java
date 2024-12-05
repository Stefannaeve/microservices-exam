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
    public ResponseEntity<ApiResponse<User>> fetchById(@PathVariable Long userId){
        ApiResponse<User> fetchById = userService.fetchUserById(userId);

        switch (fetchById){
            case ApiResponse.Success<User> success -> {
                return ResponseEntity.status(HttpStatus.OK).body(success);
            }
            case ApiResponse.Failure<User> failure -> {
                System.out.println("Something went wrong");
                return new ResponseEntity<>(failure, failure.status());
            }
        }
    }

    @PostMapping("/saveOneUser")
    public ResponseEntity<ApiResponse<User>> saveOneUser(@RequestBody User userToSave){
        ApiResponse<User> savedOneUser = userService.saveOneUser(userToSave);

        switch (savedOneUser) {
            case ApiResponse.Success<User> success -> {
                return ResponseEntity.status(HttpStatus.CREATED).body(success);
            }
            case ApiResponse.Failure<User> failure -> {
                System.out.println("Something went wrong: " + failure.errorMessage());
                return new ResponseEntity<>(failure, failure.status());
            }
        }
    }

    @GetMapping("/fetchUserBooks/{userId}")
    public ApiResponse<List<Long>> fetchUserBooks(@PathVariable Long userId){
        ApiResponse<List<Long>> fetchUserBook = userService.fetchUserBooks(userId);

        switch (fetchUserBook){
            case ApiResponse.Success<List<Long>> success -> {
                return ResponseEntity.status(HttpStatus.OK).body(success).getBody();
            }
            case ApiResponse.Failure<List<Long>> failure -> {
                System.out.println("Something went wrong");
                return new ResponseEntity<>(failure, failure.status()).getBody();
            }
        }
    }

    @PostMapping("/addBookToUser/{userId}")
    public ResponseEntity<ApiResponse<User>> addBookToUser(@PathVariable Long userId, @RequestBody UserBook userBook){
        ApiResponse<User> addBookToUser = userService.addBookToUser(userId, userBook);

        switch (addBookToUser){
            case ApiResponse.Success<User> success -> {
                return ResponseEntity.status(HttpStatus.CREATED).body(success);
            }
            case ApiResponse.Failure<User> failure -> {
                System.out.println("Something went wrong: " + failure.errorMessage());
                return new ResponseEntity<>(failure,failure.status());
            }
        }
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ApiResponse<User>> deleteUser(@PathVariable Long userId) {
        ApiResponse<User> deleteUser = userService.deleteUserById(userId);

        switch (deleteUser){
            case ApiResponse.Success<User> success -> {
                return ResponseEntity.status(HttpStatus.OK).body(success);
            }
            case ApiResponse.Failure<User> failure -> {
                System.out.println("Something went wrong: " + failure.errorMessage());
                return new ResponseEntity<>(failure, failure.status());
            }
        }
    }

    @DeleteMapping("/{userId}/deleteBook/{bookId}")
    public ResponseEntity<ApiResponse<User>> deleteBookFromUser(@PathVariable Long userId, @PathVariable Long bookId) {
        ApiResponse<User> deleteBookFromUser = userService.deleteBookFromUser(userId, bookId);

        switch (deleteBookFromUser){
            case ApiResponse.Success<User> success -> {
                return ResponseEntity.status(HttpStatus.OK).body(success);
            }
            case ApiResponse.Failure<User> failure -> {
                System.out.println("Something went wrong: " + failure.errorMessage());
                return new ResponseEntity<>(failure, failure.status());
            }
        }
    }

    @PatchMapping("/{userId}/books/{bookId}/progress")
    public ResponseEntity<ApiResponse<User>> updateReadingProgress(@PathVariable Long userId, @PathVariable Long bookId, @RequestBody Map<String, String> requestBody) {

        String newReadingProgress = requestBody.get("newReadingProgress");

        ApiResponse<User> updateReadingProgress = userService.updateReadingProgress(userId, bookId, newReadingProgress);

        switch (updateReadingProgress) {
            case ApiResponse.Success<User> success -> {
                return ResponseEntity.status(HttpStatus.OK).body(success);
            }
            case ApiResponse.Failure<User> failure -> {
                System.out.println("Something went wrong: " + failure.errorMessage());
                return new ResponseEntity<>(failure, failure.status());
            }
        }
    }
}
