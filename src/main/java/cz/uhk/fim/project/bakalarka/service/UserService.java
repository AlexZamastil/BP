package cz.uhk.fim.project.bakalarka.service;


import cz.uhk.fim.project.bakalarka.DAO.UserStatsRepository;
import cz.uhk.fim.project.bakalarka.DTO.AddAverageValuesDTO;
import cz.uhk.fim.project.bakalarka.DTO.UserDTO;
import cz.uhk.fim.project.bakalarka.model.UserStats;
import cz.uhk.fim.project.bakalarka.util.JWTUtils;
import io.micrometer.common.util.StringUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import cz.uhk.fim.project.bakalarka.model.User;

import cz.uhk.fim.project.bakalarka.DAO.UserRepository;
import cz.uhk.fim.project.bakalarka.util.MessageHandler;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Service class responsible for handling user-related operations.
 *
 * @author Alex Zamastil
 */
@Service
@Log4j2
public class UserService {
    private final JWTUtils jwtUtils;
    private final UserRepository userRepository;
    private final UserStatsRepository userStatsRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailValidator emailValidator;
    private final MessageSource messageSource;
    MessageHandler<String> messageHandler = new MessageHandler<>();

    public UserService(UserRepository userRepository, UserStatsRepository userStatsRepository, JWTUtils jwtUtils, PasswordEncoder passwordEncoder, MessageSource messageSource) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.userStatsRepository = userStatsRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailValidator = EmailValidator.getInstance();
        this.messageSource = messageSource;
    }

    /**
     * Validates if the provided nickname is valid.
     * Defines which characters can be used in a nickname.
     *
     * @param nickname The nickname to validate.
     * @return True if the nickname is valid, otherwise false.
     */
    private boolean isValidNickname(String nickname) {
        return !nickname.matches("^[a-zA-Z0-9_ ]{5,20}$");
    }

    /**
     * Handles user login authentication.
     * Verifies the provided email and password against stored credentials.
     *
     * @param email    The user's email.
     * @param password The user's password.
     * @return ResponseEntity containing the JWT token if authentication is successful, otherwise an error message.
     */
    public ResponseEntity<?> login(String email, String password) {
        System.out.println(LocaleContextHolder.getLocale());
        if (email == null || email.equals("") || password == null || password.equals(""))
            return messageHandler.error(messageSource.getMessage("error.login.null", null, LocaleContextHolder.getLocale()));
        if (userRepository.findUserByEmail(email) != null) {
            User authUser = userRepository.findUserByEmail(email);
            if (passwordEncoder.matches(password, authUser.getPassword())) {
                String jwtToken = jwtUtils.generateJWToken(authUser.getId());
                authUser.setToken(jwtToken);
                userRepository.save(authUser);
                return messageHandler.success(jwtToken);
            } else
                return messageHandler.error(messageSource.getMessage("error.password.wrong", null, LocaleContextHolder.getLocale()));
        } else
            return messageHandler.error(messageSource.getMessage("error.user.notFound", null, LocaleContextHolder.getLocale()));
    }

    /**
     * Handles password change for a user.
     * Verifies the old password, validates the new password, and updates it if all conditions are met.
     *
     * @param token       The user's authentication token.
     * @param oldPassword The old password.
     * @param newPassword The new password.
     * @return ResponseEntity indicating the success or failure of the password change operation.
     */
    public ResponseEntity<?> changePassword(String token, String oldPassword, String newPassword) {

        if (oldPassword == null || newPassword == null) {
            return messageHandler.error(messageSource.getMessage("error.changePassword.null", null, LocaleContextHolder.getLocale()));
        }
        User user = null;
        if (token != null) {
            user = userRepository.findUserById(jwtUtils.getID(token).asLong());
            if (user == null) {
                return messageHandler.error(messageSource.getMessage("error.user.notFound", null, LocaleContextHolder.getLocale()));
            }
        }

        if (!passwordEncoder.matches(oldPassword, Objects.requireNonNull(user).getPassword())) {
            return messageHandler.error(messageSource.getMessage("error.changePassword.invalid", null, LocaleContextHolder.getLocale()));
        }

        if (newPassword.length() < 8)
            return messageHandler.error(messageSource.getMessage("error.changePassword.invalidNew", null, LocaleContextHolder.getLocale()));

        if (Objects.equals(oldPassword, newPassword)) {
            return messageHandler.error(messageSource.getMessage("error.password.same", null, LocaleContextHolder.getLocale()));
        }
        String encryptedPassword = passwordEncoder.encode(newPassword);
        Objects.requireNonNull(user).setPassword(encryptedPassword);
        userRepository.save(user);
        return messageHandler.success(messageSource.getMessage("success.password.changed", null, LocaleContextHolder.getLocale()));

    }

    /**
     * Registers a new user with the provided user details.
     * Validates the provided details and creates a new user entity if all conditions are met.
     *
     * @param userDTO The DTO containing the user's details for registration.
     * @return ResponseEntity indicating the success or failure of the registration process.
     */
    public ResponseEntity<?> register(UserDTO userDTO) {

        List<String> errors = new ArrayList<>();

        if (StringUtils.isBlank(userDTO.getEmail()) ||
                StringUtils.isBlank(userDTO.getNickname()) ||
                StringUtils.isBlank(userDTO.getPassword()) ||
                userDTO.getDateOfBirth() == null ||
                userDTO.getHeight() == 0 ||
                userDTO.getWeight() == 0 ||
                userDTO.getSex() == null ||
                userDTO.getBodyType() == null)
            errors.add(messageSource.getMessage("error.register.nullValue", null, LocaleContextHolder.getLocale()));

        if (!emailValidator.isValid(userDTO.getEmail())) {
            errors.add(messageSource.getMessage("error.email.invalid", null, LocaleContextHolder.getLocale()));
        }
        if (userRepository.findUserByEmail(userDTO.getEmail()) != null) {
            errors.add(messageSource.getMessage("error.email.duplicate", null, LocaleContextHolder.getLocale()));
        }
        if (isValidNickname(userDTO.getNickname())) {
            errors.add(messageSource.getMessage("error.register.invalidNick", null, LocaleContextHolder.getLocale()));
        }

        LocalDate currentDate = LocalDate.now();
        LocalDate eighteenYearsAgo = currentDate.minusYears(18);
        LocalDate userBirthDate = userDTO.getDateOfBirth();

        if (userBirthDate.isAfter(eighteenYearsAgo)) {
            errors.add(messageSource.getMessage("error.register.ageTooYoung", null, LocaleContextHolder.getLocale()));
        }

        if (userDTO.getPassword().length() < 8)
            errors.add(messageSource.getMessage("error.register.invalidPassword", null, LocaleContextHolder.getLocale()));

        if (userDTO.getWeight() > 150 || userDTO.getWeight() < 40)
            errors.add(messageSource.getMessage("error.register.weight", null, LocaleContextHolder.getLocale()));

        if (userDTO.getHeight() > 220 || userDTO.getHeight() < 140)
            errors.add(messageSource.getMessage("error.register.height", null, LocaleContextHolder.getLocale()));

        if (!errors.isEmpty()) {
            return messageHandler.error(errors.toString());
        }


        String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
        User user = new User(userDTO.getEmail(), userDTO.getNickname(), encryptedPassword, userDTO.getDateOfBirth(), userDTO.getSex(), userDTO.getWeight(), userDTO.getHeight(), userDTO.getBodyType());
        user.setPassword(encryptedPassword);
        try {
            userRepository.save(user);
            generateUserStats(user);
        } catch (Exception e) {
            return messageHandler.error(messageSource.getMessage("error.register.dbs.failed", null, LocaleContextHolder.getLocale()));

        }
        return messageHandler.success(messageSource.getMessage("success.register.done", null, LocaleContextHolder.getLocale()));

    }

    /**
     * Retrieves user data based on the provided authentication token.
     * Fetches user stats associated with the user and returns them.
     *
     * @param header The authentication token.
     * @return ResponseEntity containing the user stats or an error message if the user is not found.
     */
    public ResponseEntity<?> getUserData(String header) {
        if (header != null) {
            User user = userRepository.findUserById(jwtUtils.getID(header).asLong());
            if (user != null) {
                log.info(user);
                UserStats userStats = userStatsRepository.findUserStatsByUser(user);
                log.info(userStats);
                return ResponseEntity.ok(userStats);
            } else {
                return messageHandler.error(messageSource.getMessage("error.user.notFound", null, LocaleContextHolder.getLocale()));
            }

        } else
            return messageHandler.error(messageSource.getMessage("error.header.invalid", null, LocaleContextHolder.getLocale()));
    }

    /**
     * Generates userStats for the user obtained from authentication token.
     *
     * @param header The authentication token.
     * @return ResponseEntity indicating the success or failure of the user statistics generation process.
     */
    public ResponseEntity<?> generateUserStats(String header) {
        User user = userRepository.findUserById(jwtUtils.getID(header).asLong());
        return generateUserStats(user);
    }

    /**
     * Generates userStats for the specified user.
     *
     * @param user The user for whom to generate statistics.
     * @return ResponseEntity indicating the success or failure of the userStats generation process.
     */
    public ResponseEntity<?> generateUserStats(User user) {
        if (user != null) {
            UserStats userStats = userStatsRepository.findUserStatsByUser(user);

            double bmi = user.getWeight() / (user.getHeight() / 100 * user.getHeight() / 100);
            bmi = Math.round(bmi * 1000.0) / 1000.0;

            double waterIntake = 0.033 * user.getWeight();
            waterIntake = Math.round(waterIntake * 1000.0) / 1000.0;

            double basalMetabolicRate;
            int userAge = Period.between(user.getDateOfBirth(), LocalDate.now()).getYears();

            if (user.getSex().toString().equals("MALE")) {
                basalMetabolicRate = 10 * user.getWeight() + 6.25 * user.getHeight() - 5 * userAge + 5;
            } else basalMetabolicRate = 10 * user.getWeight() + 6.25 * user.getHeight() - 5 * userAge - 161;

            if (userStats != null) {
                userStats.setBmr(basalMetabolicRate);
                userStats.setBmi(bmi);
                userStats.setWaterintake(waterIntake);

                userStatsRepository.save(userStats);
                return messageHandler.success("values updated into database");
            } else {
                UserStats userStats2 = new UserStats(bmi, waterIntake, basalMetabolicRate, 0, 0., user);
                userStatsRepository.save(userStats2);
                return messageHandler.success("values inserted into database");
            }
        } else return messageHandler.error("invalid user ID");
    }

    /**
     * Updates user data with the provided user details.
     *
     * @param user The user with updated details.
     * @return ResponseEntity indicating the success or failure of the user data update process.
     */
    public ResponseEntity<?> updateData(User user) {
        if (user != null) {
            List<String> errors = new ArrayList<>();
            if (isValidNickname(user.getNickname())) {
                errors.add(messageSource.getMessage("error.register.invalidNick", null, LocaleContextHolder.getLocale()));
            }
            if (!emailValidator.isValid(user.getEmail())) {
                errors.add(messageSource.getMessage("error.email.invalid", null, LocaleContextHolder.getLocale()));
            }
            if (userRepository.findUserByEmail(user.getEmail()) != null && userRepository.findUserByEmail(user.getEmail()).getId() != user.getId()) {
                errors.add(messageSource.getMessage("error.email.duplicate", null, LocaleContextHolder.getLocale()));
            }
            if (user.getWeight() > 150 || user.getWeight() < 40)
                errors.add(messageSource.getMessage("error.register.weight", null, LocaleContextHolder.getLocale()));

            if (user.getHeight() > 220 || user.getHeight() < 140)
                errors.add(messageSource.getMessage("error.register.height", null, LocaleContextHolder.getLocale()));

            LocalDate currentDate = LocalDate.now();
            LocalDate eighteenYearsAgo = currentDate.minusYears(18);
            LocalDate userBirthDate = user.getDateOfBirth();

            if (userBirthDate.isAfter(eighteenYearsAgo)) {
                errors.add(messageSource.getMessage("error.register.ageTooYoung", null, LocaleContextHolder.getLocale()));
            }

            if (!errors.isEmpty()) {
                return messageHandler.error(errors.toString());
            }

            User user2 = userRepository.findUserById(user.getId());
            user.setId(user2.getId());
            user.setPassword(user2.getPassword());
            userRepository.save(user);

            generateUserStats(user);

            return messageHandler.success(messageSource.getMessage("success.user.updated", null, LocaleContextHolder.getLocale()));
        }
        return messageHandler.error(messageSource.getMessage("error.user.notFound", null, LocaleContextHolder.getLocale()));
    }

    /**
     * Adds average values for the user. Average run length and pace are used to determine, if the training would be achievable.
     *
     * @param token               The authentication token.
     * @param addAverageValuesDTO DTO containing the average values to add.
     * @return ResponseEntity indicating the success or failure of the operation.
     */
    public ResponseEntity<?> addAverageValues(String token, AddAverageValuesDTO addAverageValuesDTO) {
        User user = userRepository.findUserByToken(token);
        UserStats userStats = userStatsRepository.findUserStatsByUser(user);
        userStats.setAverageRunLength(addAverageValuesDTO.getAverageRunLength());
        userStats.setAverageRunPace(addAverageValuesDTO.getAverageRunPace());
        updateData(user);
        return messageHandler.success(messageSource.getMessage("success.user.updated", null, LocaleContextHolder.getLocale()));
    }

    /**
     * Deletes the user obtained from the authentication token.
     *
     * @param token The authentication token.
     * @return ResponseEntity indicating the success or failure of the user deletion process.
     */
    public ResponseEntity<?> deleteUser(String token) {
        User user = userRepository.findUserByToken(token);
        if (user == null) {
            return messageHandler.error(messageSource.getMessage("error.user.notFound", null, LocaleContextHolder.getLocale()));
        }
        userRepository.deleteUserWithData(user);
        return messageHandler.success(messageSource.getMessage("success.onDelete", null, LocaleContextHolder.getLocale()));
    }

    /**
     * Logs out the user obtained from the authentication token.
     * The users JWT is deleted from the database, so it cannot be used by potential attacker after logging out.
     * This improves overall security of the project
     *
     * @param token The authentication token.
     * @return ResponseEntity indicating the success or failure of the user deletion process.
     */

    public ResponseEntity<?> logout(String token) {
        User user = userRepository.findUserByToken(token);
        if (user == null) {
            return messageHandler.error(messageSource.getMessage("error.user.notFound", null, LocaleContextHolder.getLocale()));
        }
        user.setToken("");
        userRepository.save(user);
        return messageHandler.success(messageSource.getMessage("success.onLogout", null, LocaleContextHolder.getLocale()));
    }

}
