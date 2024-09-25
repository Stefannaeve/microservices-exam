package microservices.user.user.services;

import lombok.extern.slf4j.Slf4j;
import microservices.user.user.models.User;
import microservices.user.user.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
