package microservices.user.services;

import lombok.extern.slf4j.Slf4j;
import microservices.user.models.BookId;
import microservices.user.models.User;
import microservices.user.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

    public ResponseEntity saveOneUser(User userToSave){

        try{
            User savedUser = userRepo.save(userToSave);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        }
        catch (DataIntegrityViolationException exception){
            String exceptionMessage = "Database exception: " + exception.getCause();
            log.error("{} \n", exceptionMessage);
            exception.printStackTrace();

            return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionMessage);
        }
        catch (Exception exception){
            String exceptionMessage = "Internal server error" + exception.getCause();
            log.error("{} \n", exceptionMessage);
            exception.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionMessage);
        }

    }

    public ResponseEntity fetchUserById(Long id){

        User user = userRepo.findById(id).orElse(null);
        if (user != null){
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Error message", "No matching user found").body(user);
    }

    public ResponseEntity<List<Long>> fetchUserBooks(Long userId){

        User user = userRepo.findById(userId).orElse(null);

        if (user == null){
            return ResponseEntity.status(
                    HttpStatus.NOT_FOUND).header("Error message", "No matching user found").body(null);
        }

        if(user.getBooks() != null){

            List<Long> bookIdList = new ArrayList<>();

            user.getBooks().forEach(bookId -> bookIdList.add(bookId.getId()));

            return ResponseEntity.status(HttpStatus.OK).body(bookIdList);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Error " +
                                                                          "message", "User has no books").body(null);
    }

    public ResponseEntity<User> addBookToUser(Long userId, BookId bookId){
        User user = userRepo.findById(userId).orElse(null);
        if (user == null){
            log.info(String.valueOf(user.getId()));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Error message", "No matching user found").body(null);
        }
        user.getBooks().add(bookId);
        userRepo.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
