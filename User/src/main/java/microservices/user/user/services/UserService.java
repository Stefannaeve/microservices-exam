package microservices.user.user.services;

import lombok.extern.slf4j.Slf4j;
import microservices.user.user.models.BookId;
import microservices.user.user.models.User;
import microservices.user.user.repositories.UserRepo;
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

    UserRepo userRepo;

    @Autowired
    public UserService(UserRepo userRepo) {

        this.userRepo = userRepo;
    }

    public User saveOneUser(User userToSave){
        return userRepo.save(userToSave);
    }

    public Optional<User> fetchUserById(Long id){
        return userRepo.findById(id);
    }

    public ResponseEntity<List<Long>> fetchUserBooks(Long userId){

        User user = userRepo.findById(userId).orElse(null);

        if (user == null){
            return ResponseEntity.status(
                    HttpStatus.NOT_FOUND).header("Error message", "No matching user found").body(null);
        }

        if(user.getBooks() != null){

            List<Long> bookIdList =
                    (List<Long>) user.getBooks().stream().toList().stream().mapToLong(BookId::getId);

            return ResponseEntity.status(HttpStatus.OK).body(bookIdList);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Error " +
                                                                          "message", "User has no books").body(null);
    }
}
