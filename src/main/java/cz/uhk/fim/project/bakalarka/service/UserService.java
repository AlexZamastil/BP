package cz.uhk.fim.project.bakalarka.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import cz.uhk.fim.project.bakalarka.model.User;

import cz.uhk.fim.project.bakalarka.DAO.UserRepository;
import cz.uhk.fim.project.bakalarka.util.MessageHandler;

@Service
public class UserService {
    private UserRepository userRepository;
    private MessageHandler errorHandler;

    public ResponseEntity<?> authenticateUser(String email, String password) {
        User authUser = userRepository.findUserByEmail(email);
        if (authUser == null) {
            return errorHandler.error("User not found");
        }
        if (password != authUser.getPassword()) {
            return errorHandler.error("Wrong password");
        }
        return errorHandler.success("Authenticated");

    }
}
