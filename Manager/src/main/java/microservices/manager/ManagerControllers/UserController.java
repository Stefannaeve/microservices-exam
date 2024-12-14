package microservices.manager.ManagerControllers;

import lombok.extern.slf4j.Slf4j;
import microservices.manager.ManagerClients.UserClient;
import microservices.manager.dtos.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/fetchUserById/{userId}")
    public ResponseEntity<Optional<UserDTO>> fetchUserById(@PathVariable Long userId){
        return userClient.externalGetUserById(userId);
    }

    @GetMapping("/fetchUserWithBook/{userId}/{bookId}")
    public ResponseEntity<Optional<UserDTO>> fetchUserWithBook(@PathVariable Long userId, @PathVariable Long bookId){
        return userClient.externalGetUserWithBook(userId, bookId);
    }

}
