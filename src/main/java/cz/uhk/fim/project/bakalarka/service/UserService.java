package cz.uhk.fim.project.bakalarka.service;


import cz.uhk.fim.project.bakalarka.DAO.UserStatsRepository;
import cz.uhk.fim.project.bakalarka.enumerations.Sex;
import cz.uhk.fim.project.bakalarka.model.UserStats;
import cz.uhk.fim.project.bakalarka.util.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import cz.uhk.fim.project.bakalarka.model.User;

import cz.uhk.fim.project.bakalarka.DAO.UserRepository;
import cz.uhk.fim.project.bakalarka.util.MessageHandler;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class UserService {
    private final JWTUtils jwtUtils;
    private final UserRepository userRepository;
    private final UserStatsRepository userStatsRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailValidator emailValidator;

    public UserService(UserRepository userRepository, UserStatsRepository userStatsRepository, JWTUtils jwtUtils, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.userStatsRepository = userStatsRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailValidator = EmailValidator.getInstance();
    }

    public ResponseEntity<?> login(String email, String password) {
        User authUser = userRepository.findUserByEmail(email);
        System.out.println(email);
        if (authUser == null) {
            return MessageHandler.error("User not found");
        }

        if (passwordEncoder.matches(password, authUser.getPassword())) {
            String jwttoken = jwtUtils.generateJWToken(authUser.getId());
            authUser.setToken(jwttoken);
            userRepository.save(authUser);

            return MessageHandler.success(jwttoken);
        } else return MessageHandler.error("Wrong password");

    }

    public ResponseEntity<?> changePassword(Long ID, String oldPassword, String newPassword) {
        User user = userRepository.findUserById(ID);
        if (Objects.equals(oldPassword, newPassword)) {
            return MessageHandler.error("New password must be different than the current password");
        }
        if (Objects.equals(user.getPassword(), oldPassword)) {
            user.setPassword(newPassword);
            userRepository.save(user);
            return MessageHandler.success("Password was successfully changed");
        } else return MessageHandler.error("Wrong password");
    }

    public ResponseEntity<?> register(String email, String nickname, String password, LocalDate birthdate, Sex sex) {
        if (userRepository.findUserByEmail(email) != null) {
            return MessageHandler.error("Account using this email already exist");
        } else if (!emailValidator.isValid(email)) {
            return MessageHandler.error("The entered text is not a valid email address");
        } else {
            String encryptedPassword = passwordEncoder.encode(password);
            User user = new User(email, nickname, encryptedPassword, birthdate, sex);
            userRepository.save(user);
            return MessageHandler.success("Account created successfully");
        }
    }

    public ResponseEntity<?> getUserData(String header) {
        if (header != null) {
            User user = userRepository.findUserById(jwtUtils.getID(header).asLong());
            if (user != null) {
                UserStats userStats = userStatsRepository.findUserStatsByUser(user);
                Map<String, Object> userwithstats = new HashMap<>();
                userwithstats.put("user", user);
                userwithstats.put("userstats", userStats);

                return ResponseEntity.ok(userwithstats);
            } else return MessageHandler.error("no user found");
        } else return MessageHandler.error("invalid header");

    }

    public ResponseEntity<?> generateUserStats(String header) {
        User user = userRepository.findUserById(jwtUtils.getID(header).asLong());
        if (user != null) {
            UserStats userStats = userStatsRepository.findUserStatsByUser(user);
            if (userStats != null) {
                userStats.setBmi(user.getWeight() / (user.getHeight() / 100 * user.getHeight() / 100));
                userStats.setWaterneeded(0.033 * user.getWeight());
                userStatsRepository.save(userStats);
                System.out.println("USER UPDATED" + userStats.getUser());
                return MessageHandler.success("values updated into database");
            } else {
                UserStats userStats2 = new UserStats(user.getWeight() / (user.getHeight() / 100 * user.getHeight() / 100), 0.033 * user.getWeight(), user);
                userStatsRepository.save(userStats2);
                System.out.println("USER UPDATED" + userStats2.getUser());
                return MessageHandler.success("values inserted into database");
            }
        } else return MessageHandler.error("invalid user ID");
    }

    public ResponseEntity<?> updateData(User user, HttpServletRequest httpServletRequest) {
        if (user != null) {
            User user2 = userRepository.findUserById(user.getId());
            user.setId(user2.getId());
            userRepository.save(user);
            System.out.println("Updated user: " + user);

            generateUserStats(httpServletRequest.getHeader("Authorization"));

            return MessageHandler.success("User updated");
        }
        return MessageHandler.error("user is null");

    }


}
