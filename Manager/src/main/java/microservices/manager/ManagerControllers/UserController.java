package microservices.manager.ManagerControllers;

import lombok.extern.slf4j.Slf4j;
import microservices.manager.ManagerClients.UserClient;
import microservices.manager.dtos.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/manager/user")
public class UserController {

    UserClient userClient;


    public UserController(UserClient userClient) {

        this.userClient = userClient;
    }

    @PostMapping("/saveOneUser")
    public ResponseEntity<Optional<UserDTO>> saveOneUser(@RequestBody UserDTO userDTO){
        return userClient.externalSaveOneUser(userDTO);
    }

    @PostMapping("/addBookToUser/{userId}")
    public ResponseEntity<Optional<UserDTO>> addBookToUser(@PathVariable Long userId, @RequestBody UserDTO userDTO){
        return userClient.externalAddBookToUser(userId, userDTO);
    }

    @GetMapping("/fetchUserById/{userId}")
    public ResponseEntity<Optional<UserDTO>> fetchUserById(@PathVariable Long userId){
        return userClient.externalGetUserById(userId);
    }

    @GetMapping("/checkIfUserHasBook/{userId}/{bookId}")
    public ResponseEntity<Optional<UserDTO>> checkIfUserHasBook(@PathVariable Long userId, @PathVariable Long bookId){
        return userClient.externalCheckIfUserHasBook(userId, bookId);
    }

    @GetMapping("/fetchUserBooks/{userId}")
    public ResponseEntity<Optional<UserDTO>> fetchUserBooks(@PathVariable Long userId){
        return userClient.externalFetchUserBooks(userId);
    }

    @GetMapping("/{userId}/notFinishedReading")
    public ResponseEntity<Optional<List<UserDTO>>> fetchUnfinishedBooks(@PathVariable Long userId){
        return userClient.externalFetchUnFinishedBook(userId);
    }

    @DeleteMapping("/{userId}/deleteBook/{bookId}")
    public ResponseEntity<Optional<UserDTO>> deleteBookFromUser(@PathVariable Long userId, @PathVariable Long bookId) {
        return userClient.externalDeleteBookFromUser(userId, bookId);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Optional<UserDTO>> deleteUser(@PathVariable Long userId) {
        return userClient.externalDeleteUserById(userId);
    }

    @PatchMapping("/{userId}/books/{bookId}/progress")
    public ResponseEntity<Optional<UserDTO>> updateReadingProgress(@PathVariable Long userId, @PathVariable Long bookId, @RequestBody Map<String, String> requestBody) {
        return userClient.externalUpdateReadingProgress(userId, bookId, requestBody);
    }

}
