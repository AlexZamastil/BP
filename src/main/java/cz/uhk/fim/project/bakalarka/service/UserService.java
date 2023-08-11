package cz.uhk.fim.project.bakalarka.service;

import cz.uhk.fim.project.bakalarka.util.JWTUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import cz.uhk.fim.project.bakalarka.model.User;

import cz.uhk.fim.project.bakalarka.DataAccessObjects.UserRepository;
import cz.uhk.fim.project.bakalarka.util.MessageHandler;

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
        String jwttoken = jwtUtils.generateJWToken(authUser.getEmail());
        authUser.setToken(jwttoken);
        userRepository.save(authUser);

        return MessageHandler.success(jwttoken);

    }



}
