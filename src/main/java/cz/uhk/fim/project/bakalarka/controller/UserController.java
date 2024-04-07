package cz.uhk.fim.project.bakalarka.controller;

import cz.uhk.fim.project.bakalarka.DTO.UserDTO;
import cz.uhk.fim.project.bakalarka.model.User;
import cz.uhk.fim.project.bakalarka.DTO.ChangePasswordDTO;
import cz.uhk.fim.project.bakalarka.util.AuthorizationCheck;
import cz.uhk.fim.project.bakalarka.util.MessageHandler;
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
    MessageHandler<String> messageHandler = new MessageHandler<>();

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "user/login", consumes = {"application/json"})
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        return userService.login(
                user.getEmail(),
                user.getPassword()
        );
    }

    @PostMapping(value = "user/register", consumes = {"application/json"})
    public ResponseEntity<?> registerUser(@RequestBody UserDTO user) {
        log.info(user);
        return userService.register(user);
    }
    @PostMapping(value = "user/passwordReset", consumes = {"application/json"})
    public ResponseEntity<?> newPassword(@RequestBody ChangePasswordDTO changePasswordRequest, HttpServletRequest request){
        if (!AuthorizationCheck.hasAuthorization(request)) return messageHandler.error("Missing authorization");
        return userService.changePassword(
                request.getHeader("Authorization"),
                changePasswordRequest.getOldPassword(),
                changePasswordRequest.getNewPassword()
        );
    }

    @PostMapping(value = "user/generateStats")
    public ResponseEntity<?> generateStats(HttpServletRequest request){
        if (!AuthorizationCheck.hasAuthorization(request)) return messageHandler.error("Missing authorization");
        String header = request.getHeader("Authorization");
        return userService.generateUserStats(header);
    }
    @PostMapping(value = "user/updateData",consumes = {"application/json"})
    public ResponseEntity<?> updateData(@RequestBody User user, HttpServletRequest request){
        if (!AuthorizationCheck.hasAuthorization(request)) return messageHandler.error("Missing authorization");
        return userService.updateData(user);

    }
    @GetMapping(value= "user/getUserData")
    public ResponseEntity<?> getUserData(HttpServletRequest request){
        if (!AuthorizationCheck.hasAuthorization(request)) return messageHandler.error("Missing authorization");
        String header = request.getHeader("Authorization");
        return userService.getUserData(header);
    }

    @PostMapping(value = "user/addAverageValues",consumes = {"application/json"})
    public ResponseEntity<?> addAverageValues(HttpServletRequest request, @RequestBody UserDTO userDTO){
        if (!AuthorizationCheck.hasAuthorization(request)) return messageHandler.error("Missing authorization");
        String header = request.getHeader("Authorization");
        return userService.addAverageValues(header,userDTO);
    }

}
