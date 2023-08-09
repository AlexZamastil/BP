package cz.uhk.fim.project.bakalarka.controller;

import cz.uhk.fim.project.bakalarka.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cz.uhk.fim.project.bakalarka.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {
    private UserService userService;

    @Autowired

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "basic/user/login", consumes = {"application/json"})
    public ResponseEntity<?> loginUser(@RequestBody User requestBody) {
        return userService.login(
                requestBody.getEmail(),
                requestBody.getPassword()
        );
    }
    @GetMapping(value = "test")
    public String test(){
        return "TEST";
    }
    @PostMapping("test-request")
    public ResponseEntity<String> testPostRequest() {
        return ResponseEntity.ok("POST request successful");
    }
}
