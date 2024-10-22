package microservices.user.controllers;

import lombok.extern.slf4j.Slf4j;
import microservices.user.models.BookId;
import microservices.user.models.User;
import microservices.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
    public ResponseEntity<User> fetchById(@PathVariable Long userId){

        return userService.fetchUserById(userId);
    }

    @PostMapping("/saveOneUser")
    public ResponseEntity<User> saveOneUser(@RequestBody User userToSave){

        return userService.saveOneUser(userToSave);
    }

    @GetMapping("/fetchUserBooks/{userId}")
    public ResponseEntity<List<Long>> fetchUserBooks(@PathVariable Long userId){

        return userService.fetchUserBooks(userId);
    }
    @PostMapping("/addBookToUser/{userId}")
    public ResponseEntity addBookToUser(@PathVariable Long userId, @RequestBody BookId bookId){

        return userService.addBookToUser(userId, bookId);
    }
}
