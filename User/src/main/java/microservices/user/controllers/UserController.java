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
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/fetchUserById/{userId}")
    public ResponseEntity<Optional<User>> fetchById(@PathVariable Long userId) {
        return userService.fetchUserById(userId);
    }

    @PostMapping("/saveOneUser")
    public ResponseEntity<Optional<User>> saveOneUser(@RequestBody User userToSave) {
        return userService.saveOneUser(userToSave);
    }

    @GetMapping("/fetchUserBooks/{userId}")
    public ResponseEntity<Optional<List<Long>>> fetchUserBooks(@PathVariable Long userId) {
        return userService.fetchUserBooks(userId);
    }

    @PostMapping("/addBookToUser/{userId}")
    public ResponseEntity<Optional<User>> addBookToUser(@PathVariable Long userId, @RequestBody UserBook userBook) {
        return userService.addBookToUser(userId, userBook);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Optional<User>> deleteUser(@PathVariable Long userId) {
        return userService.deleteUserById(userId);
    }

    @DeleteMapping("/{userId}/deleteBook/{bookId}")
    public ResponseEntity<Optional<User>> deleteBookFromUser(@PathVariable Long userId, @PathVariable Long bookId) {
        return userService.deleteBookFromUser(userId, bookId);
    }

    @PatchMapping("/{userId}/books/{bookId}/progress")
    public ResponseEntity<Optional<User>> updateReadingProgress(@PathVariable Long userId, @PathVariable Long bookId, @RequestBody Map<String, String> requestBody) {
        String newReadingProgress = requestBody.get("newReadingProgress");
        String newReadingStatus = requestBody.get("newReadingStatus");
        return userService.updateReadingProgress(userId, bookId, newReadingProgress, newReadingStatus);
    }

    @GetMapping("/fetchUserWithBook/{userId}/{bookId}")
    public ResponseEntity<Optional<User>> fetchUserWithBook(@PathVariable Long userId, @PathVariable Long bookId){
        return userService.fetchUserWithBook(userId, bookId);
    }

    @GetMapping("/{userId}/notFinishedReading")
    public ResponseEntity<Optional<List<UserBook>>> fetchUnfinishedBooks(@PathVariable Long userId) {
        return userService.fetchNotFinishedBooks(userId);
    }
}
