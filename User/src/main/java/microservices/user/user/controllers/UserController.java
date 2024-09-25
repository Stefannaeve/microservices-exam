package microservices.user.user.controllers;

import microservices.user.user.models.BookId;
import microservices.user.user.models.User;
import microservices.user.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {

    UserService userService;


    @Autowired
    public UserController(UserService userService) {

        this.userService = userService;
    }

    @GetMapping("/fetchById/{userId}")
    public ResponseEntity<User> fetchById(@PathVariable Long userId){

        User user = userService.fetchUserById(userId).orElse(null);
        if (user != null){
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Error message", "No matching user found").body(user);
    }

    @PostMapping("/saveOneUser")
    public ResponseEntity<User> saveOneUser(@RequestBody User userToSave){

        User savedUser = userService.saveOneUser(userToSave);
        if(savedUser != null){
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("Error message", "something went wrong").body(userToSave);
    }

    @GetMapping("/fetchUserBooks/{userId}")
    public ResponseEntity<List<BookId>> fetchUserBooks(@PathVariable Long userId){

        User user = userService.fetchUserById(userId).orElse(null);
        if (user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Error message", "No matching user found").body(null);
        }

        if(user.getBooks() != null){
            return ResponseEntity.status(HttpStatus.OK).body(user.getBooks());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Error message", "User has no books").body(user.getBooks());
    }

    @PostMapping("/user/addBookToUser/{userId}")
    public ResponseEntity addBookToUser(@PathVariable long userId, @RequestBody BookId bookId){

        User user = userService.fetchUserById(userId).orElse(null);
        if (user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Error message", "No matching user found").body(null);
        }
        user.getBooks().add(bookId);
        userService.saveOneUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
