package cz.uhk.fim.project.bakalarka.controller;

import cz.uhk.fim.project.bakalarka.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cz.uhk.fim.project.bakalarka.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {
    private UserService userService;

    @Autowired

    public UserController(UserService userService) {
        this.userService = userService;
    }



    @PostMapping(value = "/user/login", consumes = {"application/json"})
    public ResponseEntity<?> authenticateUser(@RequestBody User requestBody) {
        return userService.authenticateUser(
                requestBody.getEmail(),
                requestBody.getPassword()
        );
    }
}
