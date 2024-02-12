package cz.uhk.fim.project.bakalarka.service;


import cz.uhk.fim.project.bakalarka.DAO.UserStatsRepository;
import cz.uhk.fim.project.bakalarka.dto.UserDto;
import cz.uhk.fim.project.bakalarka.enumerations.Sex;
import cz.uhk.fim.project.bakalarka.model.UserStats;
import cz.uhk.fim.project.bakalarka.util.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
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

@Log4j2
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
        System.out.println("X");
        User authUser = userRepository.findUserByEmail(email);
        System.out.println(email);
        if (authUser == null) {
            return MessageHandler.error("User not found");
        }

        if (passwordEncoder.matches(password, authUser.getPassword())) {
            String jwtToken = jwtUtils.generateJWToken(authUser.getId());
            authUser.setToken(jwtToken);
            userRepository.save(authUser);

            return MessageHandler.success(jwtToken);
        } else return MessageHandler.error("Wrong password");

    }

    public ResponseEntity<?> changePassword(Long ID, String oldPassword, String newPassword) {
        User user = userRepository.findUserById(ID);
        if (Objects.equals(oldPassword, newPassword)) {
            return MessageHandler.error("New password must be different");
        }
        if (Objects.equals(user.getPassword(), oldPassword)) {
            user.setPassword(newPassword);
            userRepository.save(user);
            return MessageHandler.success("Password was successfully changed");
        } else return MessageHandler.error("Wrong password");
    }

    public ResponseEntity<?> register(String email, String nickname, String password, LocalDate birthdate, Sex sex) {
        // FIXME Analogicky dle komentářů lze doplnit i ostatní business logiku

        // FIXME Místo předávání jednotlivých parametrů je lepší již v kontroléru předávat hodnoty v JSONu a mapovat je na vlastní DTO (vlastní třídy) a po validaci z nich vytvořit entity v DB

        // FIXME Validaci vstupních údajů by měl být jeden z první kroků!
        // Například, když by email obsahoval nějaký script pro DB typu SQL injection, mohl byste zde (například) přijít o data
        if (userRepository.findUserByEmail(email) != null) {
            return MessageHandler.error("Account using this email already exist");
            // FIXME Nic proti tomuto typu validace, ale raději bych šel (například) cestou regulárních výrazů a validoval tak veškeré atributy
        } else if (!emailValidator.isValid(email)) {
//            FIXME Bylo by dobré uživateli vrátit informaci o tom, proč email není validní?
            return MessageHandler.error("The entered text is not a valid email address");
        } else {
            String encryptedPassword = passwordEncoder.encode(password);
            User user = new User(email, nickname, encryptedPassword, birthdate, sex);
            userRepository.save(user);
            return MessageHandler.success("Account created successfully");
        }
    }

    public ResponseEntity<?> registerExample(UserDto dto) {
        log.info("");// FIXME

        if (!emailValidator.isValid(dto.getEmail())) {
            return MessageHandler.error("The entered text is not a valid email address");
        } else if (userRepository.findUserByEmail(dto.getEmail()) != null) {
            return MessageHandler.error("Account using this email already exist");
            // FIXME Nic proti tomuto typu validace, ale raději bych šel (například) cestou regulárních výrazů a validoval tak veškeré atributy
        }

        String encryptedPassword = passwordEncoder.encode(dto.getPassword());
        // FIXME Alternativě lze místo volání konstruktoru využít ModelMapper či ObjectMapper
        User user = new User(dto.getEmail(), dto.getNickname(), encryptedPassword, dto.getBirthdate(), dto.getSex());
        try {
            userRepository.save(user);
        } catch (Exception e) {
            log.error("An error occurred while trying to save (/register) a new user.", e);
            return MessageHandler.error("An error occurred while trying to save (/register) a new user.");
        }
        return MessageHandler.success("Account created successfully");
    }

    public ResponseEntity<?> getUserData(String header) {
        if (header != null) {
            User user = userRepository.findUserById(jwtUtils.getID(header).asLong());
            if (user != null) {
                UserStats userStats = userStatsRepository.findUserStatsByUser(user);
                Map<String, Object> userWithStats = new HashMap<>();
                userWithStats.put("user", user);
                userWithStats.put("userstats", userStats);

                return ResponseEntity.ok(userWithStats);
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

    public ResponseEntity<?> generateUserStats(User user) {
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
        return MessageHandler.error("user not found");

    }


}
