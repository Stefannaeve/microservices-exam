package microservices.manager.ManagerControllers;

import lombok.extern.slf4j.Slf4j;
import microservices.manager.ManagerClients.UserClient;
import microservices.manager.dtos.UserDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/manager/user")
public class UserController {

    UserClient userClient;


    public UserController(UserClient userClient) {

        this.userClient = userClient;
    }

    @GetMapping("/fetchUserById/{userId}")
    public UserDTO fetchUserById(@PathVariable String userId){
        return userClient.externalGetUserById(userId);
    }

}
