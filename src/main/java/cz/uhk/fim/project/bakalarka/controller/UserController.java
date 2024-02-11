package cz.uhk.fim.project.bakalarka.controller;

import cz.uhk.fim.project.bakalarka.model.User;
import cz.uhk.fim.project.bakalarka.request.ChangePasswordRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cz.uhk.fim.project.bakalarka.service.UserService;

@Log4j2
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "unauthorized/user/login", consumes = {"application/json"})
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        return userService.login(
                user.getEmail(),
                user.getPassword()
        );
    }

    @PostMapping(value = "unauthorized/user/register", consumes = {"application/json"})
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        log.info(user);
        userService.generateUserStats(user);
        return userService.register(
                user.getEmail(),
                user.getNickname(),
                user.getPassword(),
                user.getDateOfBirth(),
                user.getSex()
        );
    }
    @PostMapping(value = "authorized/user/passwordReset", consumes = {"application/json"})
    public ResponseEntity<?> newPassword(@RequestBody ChangePasswordRequest changePasswordRequest){
        return userService.changePassword(
                changePasswordRequest.getUserId(),
                changePasswordRequest.getOldPassword(),
                changePasswordRequest.getNewPassword()
        );
    }

    @PostMapping(value = "authorized/user/generateStats")
    public ResponseEntity<?> generateStats(HttpServletRequest httpServletRequest){
        String header = httpServletRequest.getHeader("Authorization");
        return userService.generateUserStats(header);
    }
    @PostMapping(value = "authorized/user/updateData",consumes = {"application/json"})
    public ResponseEntity<?> updateData(@RequestBody User user, HttpServletRequest httpServletRequest){
        return userService.updateData(user, httpServletRequest);

    }
    @GetMapping(value= "authorized/user/getUserData")
    public ResponseEntity<?> getUserData(HttpServletRequest httpServletRequest){
        String header = httpServletRequest.getHeader("Authorization");
        return userService.getUserData(header);
    }

}
