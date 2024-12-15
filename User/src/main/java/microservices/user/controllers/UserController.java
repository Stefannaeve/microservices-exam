package microservices.user.controllers;

import lombok.extern.slf4j.Slf4j;
import microservices.user.models.User;
import microservices.user.models.UserBook;
import microservices.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/saveOneUser")
    public ResponseEntity<Optional<User>> saveOneUser(@RequestBody User userToSave) {
        return userService.saveOneUser(userToSave);
    }

    @PostMapping("/addBookToUser/{userId}")
    public ResponseEntity<Optional<User>> addBookToUser(@PathVariable Long userId, @RequestBody UserBook userBook) {
        return userService.addBookToUser(userId, userBook);
    }

    @GetMapping("/fetchUserById/{userId}")
    public ResponseEntity<Optional<User>> fetchById(@PathVariable Long userId) {
        return userService.fetchUserById(userId);
    }

    @GetMapping("/fetchUserWithBook/{userId}/{bookId}")
    public ResponseEntity<Optional<User>> fetchUserWithBook(@PathVariable Long userId, @PathVariable Long bookId){
        return userService.fetchUserWithBook(userId, bookId);
    }

    @GetMapping("/fetchUserBooks/{userId}")
    public ResponseEntity<Optional<List<Long>>> fetchUserBooks(@PathVariable Long userId) {
        return userService.fetchUserBooks(userId);
    }

    @GetMapping("/{userId}/notFinishedReading")
    public ResponseEntity<Optional<List<UserBook>>> fetchUnfinishedBooks(@PathVariable Long userId) {
        return userService.fetchNotFinishedBooks(userId);
    }

    @DeleteMapping("/{userId}/deleteBook/{bookId}")
    public ResponseEntity<Optional<User>> deleteBookFromUser(@PathVariable Long userId, @PathVariable Long bookId) {
        return userService.deleteBookFromUser(userId, bookId);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Optional<User>> deleteUser(@PathVariable Long userId) {
        return userService.deleteUserById(userId);
    }

    @PatchMapping("/{userId}/books/{bookId}/progress")
    public ResponseEntity<Optional<User>> updateReadingProgress(@PathVariable Long userId, @PathVariable Long bookId, @RequestBody Map<String, String> requestBody) {
        return userService.updateReadingProgress(userId, bookId, requestBody);
    }
}
