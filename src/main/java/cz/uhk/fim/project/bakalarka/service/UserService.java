package cz.uhk.fim.project.bakalarka.service;

import cz.uhk.fim.project.bakalarka.util.JWTUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import cz.uhk.fim.project.bakalarka.model.User;

import cz.uhk.fim.project.bakalarka.DataAccessObjects.UserRepository;
import cz.uhk.fim.project.bakalarka.util.MessageHandler;

import java.time.LocalDate;
import java.util.Objects;

@Service
public class UserService {
   private final JWTUtils jwtUtils;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository, JWTUtils jwtUtils) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    public ResponseEntity<?> login(String email, String password) {
        User authUser = userRepository.findUserByEmail(email);
        if (authUser == null) {
            return MessageHandler.error("User not found");
        }

        if (!Objects.equals(password, authUser.getPassword())) {

            return MessageHandler.error("Wrong password");
        }
        String jwttoken = jwtUtils.generateJWToken(authUser.getId());
        authUser.setToken(jwttoken);
        userRepository.save(authUser);

        return MessageHandler.success(jwttoken);

    }

    public ResponseEntity<?> changePassword(Long ID, String oldPassword, String newPassword) {
        User user = userRepository.findUserById(ID);
        if(Objects.equals(oldPassword,newPassword)){
            return MessageHandler.error("New password must be different than the current password");
        }
        if (Objects.equals(user.getPassword(), oldPassword)){
            user.setPassword(newPassword);
            userRepository.save(user);
            return MessageHandler.success("Password was successfully changed");
        }
        else return MessageHandler.error("Wrong password");
    }

    public ResponseEntity<?> register(String email, String nickname, String password, LocalDate birthdate){
        if (userRepository.findUserByEmail(email) != null){
            return MessageHandler.error("Account using this email already exist");
            }
        else {
            User user = new User(email,nickname,password,birthdate);
            userRepository.save(user);
            return MessageHandler.success("Account created successfully");
        }
    }



}
