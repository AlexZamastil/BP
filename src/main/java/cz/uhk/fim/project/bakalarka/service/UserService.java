package cz.uhk.fim.project.bakalarka.service;


import cz.uhk.fim.project.bakalarka.DAO.UserStatsRepository;
import cz.uhk.fim.project.bakalarka.DTO.UserDTO;
import cz.uhk.fim.project.bakalarka.model.UserStats;
import cz.uhk.fim.project.bakalarka.util.JWTUtils;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import cz.uhk.fim.project.bakalarka.model.User;

import cz.uhk.fim.project.bakalarka.DAO.UserRepository;
import cz.uhk.fim.project.bakalarka.util.MessageHandler;

import java.util.Objects;

@Service
public class UserService {
    private final JWTUtils jwtUtils;
    private final UserRepository userRepository;
    private final UserStatsRepository userStatsRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailValidator emailValidator;
    private final MessageSource messageSource;

    private boolean isValidNickname(String nickname) {
        return nickname.matches("^[a-zA-Z0-9_ ]{5,20}$");
    }

    public UserService(UserRepository userRepository, UserStatsRepository userStatsRepository, JWTUtils jwtUtils, PasswordEncoder passwordEncoder, MessageSource messageSource) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.userStatsRepository = userStatsRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailValidator = EmailValidator.getInstance();
        this.messageSource = messageSource;
    }

    public ResponseEntity<?> login(String email, String password) {
        System.out.println(LocaleContextHolder.getLocale());
        if (email == null || email.equals("") || password == null || password.equals(""))
            return MessageHandler.error(messageSource.getMessage("error.login.null", null, LocaleContextHolder.getLocale()));
        if (userRepository.findUserByEmail(email) != null) {
            User authUser = userRepository.findUserByEmail(email);
            if (passwordEncoder.matches(password, authUser.getPassword())) {
                String jwtToken = jwtUtils.generateJWToken(authUser.getId());
                authUser.setToken(jwtToken);
                userRepository.save(authUser);
                return MessageHandler.success(jwtToken);
            } else
                return MessageHandler.error(messageSource.getMessage("error.password.wrong", null, LocaleContextHolder.getLocale()));
        } else
            return MessageHandler.error(messageSource.getMessage("error.user.notFound", null, LocaleContextHolder.getLocale()));
    }

    public ResponseEntity<?> changePassword(String token, String oldPassword, String newPassword) {

        if(oldPassword == null || newPassword == null){
            return MessageHandler.error(messageSource.getMessage("error.changePassword.null", null, LocaleContextHolder.getLocale()));
        }
        User user = null;
        if (token != null) {
            user = userRepository.findUserById(jwtUtils.getID(token).asLong());
            if (user == null) {
                return MessageHandler.error(messageSource.getMessage("error.user.notFound", null, LocaleContextHolder.getLocale()));
            }
        }

        if (!passwordEncoder.matches(oldPassword, Objects.requireNonNull(user).getPassword())) {
            return MessageHandler.error(messageSource.getMessage("error.changePassword.invalid", null, LocaleContextHolder.getLocale()));
        }

        if(newPassword.length() < 8) return MessageHandler.error(messageSource.getMessage("error.changePassword.invalidNew", null, LocaleContextHolder.getLocale()));

        if (Objects.equals(oldPassword, newPassword)) {
            return MessageHandler.error(messageSource.getMessage("error.password.same", null, LocaleContextHolder.getLocale()));
        }
            String encryptedPassword = passwordEncoder.encode(newPassword);
            Objects.requireNonNull(user).setPassword(encryptedPassword);
            userRepository.save(user);
            return MessageHandler.success(messageSource.getMessage("success.password.changed", null, LocaleContextHolder.getLocale()));

    }

    public ResponseEntity<?> register(UserDTO userDTO) {
        if (StringUtils.isBlank(userDTO.getEmail()) ||
                StringUtils.isBlank(userDTO.getNickname()) ||
                StringUtils.isBlank(userDTO.getPassword()) ||
                userDTO.getDateOfBirth() == null ||
                userDTO.getHeight() == 0 ||
                userDTO.getWeight() == 0 ||
                userDTO.getSex() == null)
            return MessageHandler.error(messageSource.getMessage("error.register.nullValue", null, LocaleContextHolder.getLocale()));

        if (!emailValidator.isValid(userDTO.getEmail())) {
            return MessageHandler.error(messageSource.getMessage("error.email.invalid", null, LocaleContextHolder.getLocale()));
        }
        if (userRepository.findUserByEmail(userDTO.getEmail()) != null) {
            return MessageHandler.error(messageSource.getMessage("error.email.duplicate", null, LocaleContextHolder.getLocale()));
        }
        if (!isValidNickname(userDTO.getNickname())) {
            return MessageHandler.error(messageSource.getMessage("error.register.invalidNick", null, LocaleContextHolder.getLocale()));
        }
        if(userDTO.getPassword().length() < 8) return MessageHandler.error(messageSource.getMessage("error.register.invalidPassword", null, LocaleContextHolder.getLocale()));

        String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
        User user = new User(userDTO.getEmail(), userDTO.getNickname(), encryptedPassword, userDTO.getDateOfBirth(), userDTO.getSex(), userDTO.getWeight(), userDTO.getHeight(),userDTO.getBodyType());
        user.setPassword(encryptedPassword);
        try {
            userRepository.save(user);
            generateUserStats(user);
        } catch (Exception e) {
            return MessageHandler.error(messageSource.getMessage("error.register.dbs.failed", null, LocaleContextHolder.getLocale()));

        }
        return MessageHandler.success(messageSource.getMessage("success.register.done", null, LocaleContextHolder.getLocale()));

    }

    public ResponseEntity<?> getUserData(String header) {
        if (header != null) {
            User user = userRepository.findUserById(jwtUtils.getID(header).asLong());
            if (user != null) {
                UserStats userStats = userStatsRepository.findUserStatsByUser(user);

                return ResponseEntity.ok(userStats);
            } else
                return MessageHandler.error(messageSource.getMessage("error.user.notFound", null, LocaleContextHolder.getLocale()));
        } else
            return MessageHandler.error(messageSource.getMessage("error.header.invalid", null, LocaleContextHolder.getLocale()));


    }

    public ResponseEntity<?> generateUserStats(String header) {
        User user = userRepository.findUserById(jwtUtils.getID(header).asLong());
        return generateUserStats(user);
    }

    public ResponseEntity<?> generateUserStats(User user) {
        if (user != null) {
            UserStats userStats = userStatsRepository.findUserStatsByUser(user);

            double bmi = user.getWeight() / (user.getHeight() / 100 * user.getHeight() / 100);
            bmi = Math.round(bmi * 1000.0) / 1000.0;

            double waterIntake = 0.033 * user.getWeight();
            waterIntake = Math.round(waterIntake * 1000.0) / 1000.0;

            if (userStats != null) {

                userStats.setBmi(bmi);
                userStats.setWaterintake(waterIntake);

                userStatsRepository.save(userStats);
                return MessageHandler.success("values updated into database");
            } else {
                UserStats userStats2 = new UserStats(bmi, waterIntake, user);
                userStatsRepository.save(userStats2);
                return MessageHandler.success("values inserted into database");
            }
        } else return MessageHandler.error("invalid user ID");
    }


    public ResponseEntity<?> updateData(User user, HttpServletRequest httpServletRequest) {
        if (user != null) {

            if (!isValidNickname(user.getNickname())) {
                return MessageHandler.error(messageSource.getMessage("error.register.invalidNick", null, LocaleContextHolder.getLocale()));
            }

            if (!emailValidator.isValid(user.getEmail())) {
                return MessageHandler.error(messageSource.getMessage("error.email.invalid", null, LocaleContextHolder.getLocale()));
            }
            if (userRepository.findUserByEmail(user.getEmail()) != null && userRepository.findUserByEmail(user.getEmail()).getId() != user.getId()) {
                return MessageHandler.error(messageSource.getMessage("error.email.duplicate", null, LocaleContextHolder.getLocale()));
            }

            User user2 = userRepository.findUserById(user.getId());
            user.setId(user2.getId());
            userRepository.save(user);

            generateUserStats(httpServletRequest.getHeader("Authorization"));

            return MessageHandler.success(messageSource.getMessage("success.user.updated", null, LocaleContextHolder.getLocale()));
        }
        return MessageHandler.error(messageSource.getMessage("error.user.notFound", null, LocaleContextHolder.getLocale()));

    }

}
