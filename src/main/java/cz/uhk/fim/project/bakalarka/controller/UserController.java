package cz.uhk.fim.project.bakalarka.controller;

import cz.uhk.fim.project.bakalarka.DTO.AddAverageValuesDTO;
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

/**
 * Controller class for handling user-related endpoints.
 *
 * @author Alex Zamastil
 */
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

    /**
     * Endpoint for user login.
     *
     * @param user The user object containing email and password.
     * @return ResponseEntity with the result of the login operation.
     */
    @PostMapping(value = "user/login", consumes = {"application/json"})
    public ResponseEntity<?> loginUser(@RequestBody UserDTO user) {
        return userService.login(
                user.getEmail(),
                user.getPassword()
        );
    }

    /**
     * Endpoint for user registration.
     *
     * @param user The DTO containing user registration details.
     * @return ResponseEntity with the result of the registration operation.
     */
    @PostMapping(value = "user/register", consumes = {"application/json"})
    public ResponseEntity<?> registerUser(@RequestBody UserDTO user) {
        log.info(user);
        return userService.register(user);
    }

    /**
     * Endpoint for resetting user password.
     *
     * @param changePasswordRequest The DTO containing old and new passwords.
     * @param request               The HttpServletRequest for authorization check and obtaining user ID.
     * @return ResponseEntity with the result of the password reset operation.
     */
    @PostMapping(value = "user/passwordReset", consumes = {"application/json"})
    public ResponseEntity<?> newPassword(@RequestBody ChangePasswordDTO changePasswordRequest, HttpServletRequest request) {
        if (!AuthorizationCheck.hasAuthorization(request)) return messageHandler.error("Missing authorization");
        return userService.changePassword(
                request.getHeader("Authorization"),
                changePasswordRequest.getOldPassword(),
                changePasswordRequest.getNewPassword()
        );
    }

    /**
     * Endpoint for generating additional user data - BMI and water intake.
     *
     * @param request The HttpServletRequest for authorization check and obtaining user ID.
     * @return ResponseEntity with the result of the operation.
     */

    @PostMapping(value = "user/generateStats")
    public ResponseEntity<?> generateStats(HttpServletRequest request) {
        if (!AuthorizationCheck.hasAuthorization(request)) return messageHandler.error("Missing authorization");
        String header = request.getHeader("Authorization");
        return userService.generateUserStats(header);
    }

    /**
     * Endpoint for updating user data. Does not work for password.
     *
     * @param user    The data to overwrite current user data.
     * @param request The HttpServletRequest for authorization check and obtaining user ID.
     * @return ResponseEntity with the result of the operation.
     */
    @PostMapping(value = "user/updateData", consumes = {"application/json"})
    public ResponseEntity<?> updateData(@RequestBody User user, HttpServletRequest request) {
        if (!AuthorizationCheck.hasAuthorization(request)) return messageHandler.error("Missing authorization");
        return userService.updateData(user);

    }

    /**
     * Endpoint for retrieving user data.
     *
     * @param request The HttpServletRequest for authorization check and obtaining user ID.
     * @return ResponseEntity with the user data.
     */
    @GetMapping(value = "user/getUserData")
    public ResponseEntity<?> getUserData(HttpServletRequest request) {
        if (!AuthorizationCheck.hasAuthorization(request)) return messageHandler.error("Missing authorization");
        String header = request.getHeader("Authorization");
        return userService.getUserData(header);
    }

    /**
     * Endpoint for adding users average values. These values are used to calculate possibility of training completion.
     *
     * @param request             The HttpServletRequest for authorization check and obtaining user ID.
     * @param addAverageValuesDTO The AddAverageValuesDTO containing average values.
     * @return ResponseEntity with the result of the operation.
     */
    @PostMapping(value = "user/addAverageValues", consumes = {"application/json"})
    public ResponseEntity<?> addAverageValues(HttpServletRequest request, @RequestBody AddAverageValuesDTO addAverageValuesDTO) {
        if (!AuthorizationCheck.hasAuthorization(request)) return messageHandler.error("Missing authorization");
        String header = request.getHeader("Authorization");
        return userService.addAverageValues(header, addAverageValuesDTO);
    }
    /**
     * Endpoint for deleting a user account.
     *
     * @param request The HttpServletRequest for authorization check and obtaining user ID.
     * @return ResponseEntity with the result of the operation.
     */
    @DeleteMapping(value = "user/deleteUser")
    public ResponseEntity<?> deleteUser(HttpServletRequest request) {
        if (!AuthorizationCheck.hasAuthorization(request)) return messageHandler.error("Missing authorization");
        String header = request.getHeader("Authorization");
        return userService.deleteUser(header);
    }
    /**
     * Endpoint for logging out of a user account.
     *
     * @param request The HttpServletRequest for authorization check and obtaining user ID.
     * @return ResponseEntity with the result of the operation.
     */
    @PostMapping(value = "user/logout")
    public ResponseEntity<?>logoutUser(HttpServletRequest request) {
        if (!AuthorizationCheck.hasAuthorization(request)) return messageHandler.error("Missing authorization");
        String header = request.getHeader("Authorization");
        return userService.logout(header);
    }

}
