package com.mymsn.endpoints;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mymsn.entities.User;
import com.mymsn.services.UserService;

@RestController
@RequestMapping("/api")
public class UserEndpoint {
    private final UserService userService;

    @Autowired
    public UserEndpoint(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{login}")
    public ResponseEntity<User> getUserByLogin(@PathVariable(name = "login") String login) {
        Optional<User> resultQuery = this.userService.findUserByLogin(login);
        return resultQuery.isPresent() ? ResponseEntity.ok().body(resultQuery.get())
                : ResponseEntity.notFound().build();
    }
}
