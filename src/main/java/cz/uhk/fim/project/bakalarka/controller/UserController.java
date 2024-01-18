package cz.uhk.fim.project.bakalarka.controller;

import cz.uhk.fim.project.bakalarka.model.User;
import cz.uhk.fim.project.bakalarka.request.ChangePasswordRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cz.uhk.fim.project.bakalarka.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "nonauthorized/user/login", consumes = {"application/json"})
    public ResponseEntity<?> loginUser(@RequestBody User user, HttpServletRequest request) {
        String csrfToken = request.getHeader("X-XSRF-TOKEN");
        System.out.println("CSRF TOKEN: " + csrfToken);
        return userService.login(
                user.getEmail(),
                user.getPassword()
        );
    }

    @PostMapping(value = "nonauthorized/user/register", consumes = {"application/json"})
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        System.out.println(user);
        return userService.register(
                user.getEmail(),
                user.getNickname(),
                user.getPassword(),
                user.getDateOfBirth()
        );
    }
    @PostMapping(value = "authorized/user/passwordreset", consumes = {"application/json"})
    public ResponseEntity<?> newPassword(@RequestBody ChangePasswordRequest changePasswordRequest){
        return userService.changePassword(
                changePasswordRequest.getUserId(),
                changePasswordRequest.getOldPassword(),
                changePasswordRequest.getNewPassword()
        );
    }

    @PostMapping(value = "authorized/user/generatestats")
    public ResponseEntity<?> generateStats(HttpServletRequest httpServletRequest){
        String header = httpServletRequest.getHeader("Authorization");
        return userService.generateUserStats(header);
    }
    @PostMapping(value = "authorized/user/updateData",consumes = {"application/json"})
    public ResponseEntity<?> updateData(@RequestBody User user, HttpServletRequest httpServletRequest){
        return userService.updateData(user, httpServletRequest);

    }

    @GetMapping(value= "authorized/user/getuserdata")
    public ResponseEntity<?> getUserData(HttpServletRequest httpServletRequest){
        String header = httpServletRequest.getHeader("Authorization");
        return userService.getUserData(header);
    }
    @GetMapping(value = "authorized/test")
    public String test(){
        return "TEST";
    }

    @PostMapping("authorized/test-request")
    public ResponseEntity<String> testPostRequest() {
        return ResponseEntity.ok("POST request successful");
    }
}
