package cz.uhk.fim.project.bakalarka.service;

import cz.uhk.fim.project.bakalarka.DAO.UserRepository;
import cz.uhk.fim.project.bakalarka.DAO.UserStatsRepository;
import cz.uhk.fim.project.bakalarka.DTO.UserDTO;
import cz.uhk.fim.project.bakalarka.enumerations.BodyType;
import cz.uhk.fim.project.bakalarka.enumerations.Sex;
import cz.uhk.fim.project.bakalarka.model.User;
import cz.uhk.fim.project.bakalarka.model.UserStats;
import cz.uhk.fim.project.bakalarka.util.JWTUtils;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@Log4j2
@SpringBootTest
@WebAppConfiguration
class UserServiceTest {

    private final UserRepository userRepositoryMock = mock(UserRepository.class);
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private UserStatsRepository userStatsRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @BeforeEach
    void setUp() {
        log.info("Resetting environment");
        userStatsRepository.deleteAll();
        userRepository.deleteAll();

        reset(userRepositoryMock);
        clearInvocations(userRepositoryMock);
    }

    @AfterEach
    void tearDown() {
        log.info("Emptying database");
        userStatsRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void login_Hds() {
        log.info("Creating user entity and logging in via users credentials");
        // Checking for empty database
        assertThat(userRepository.findAll(), empty());

        // Data preparation
        String email = "home.simpson@uhk.cz";
        String nickname = "homer1";
        String password = "Passwd_123!";
        LocalDate birthdate = LocalDate.of(2000, 1, 1);
        int height = 178;
        int weight = 70;
        Sex sex = Sex.MALE;
        BodyType bodyType = BodyType.AVERAGE;

        UserDTO dto = new UserDTO(birthdate, email, nickname, password, sex, weight, height, bodyType, 0, 0);

        // Execution
        userService.register(dto);
        ResponseEntity<?> responseEntity = userService.login(email,password);
        String token = Objects.requireNonNull(responseEntity.getBody()).toString();
        User user = userRepository.findUserByEmail(email);

        // ID Verification
        assertThat( jwtUtils.getID(token).asLong(), is( user.getId()));
    }

    @Test
    void login_WrongPassword() {
        log.info("Creating user entity and logging in via wrong password. Access should be rejected");
        // Checking for empty database
        assertThat(userRepository.findAll(), empty());

        // Data preparation
        String email = "home.simpson@uhk.cz";
        String nickname = "homer1";
        String password = "Passwd_123!";
        String wrongPassword = "wrong_password";
        LocalDate birthdate = LocalDate.of(2000, 1, 1);
        int height = 178;
        int weight = 70;
        Sex sex = Sex.MALE;
        BodyType bodyType = BodyType.AVERAGE;

        UserDTO dto = new UserDTO(birthdate, email, nickname, password, sex, weight, height, bodyType, 0, 0);

        // Execution
        userService.register(dto);
        ResponseEntity<?> responseEntity = userService.login(email,wrongPassword);

        // Verification
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertThat(responseEntity.getBody(), is("Neplatné heslo"));
    }

    @Test
    void login_NonexistentUser() {
        log.info("Logging in via made up credentials. Access should be rejected");
        // Checking for empty database
        assertThat(userRepository.findAll(), empty());
        // Data preparation
        String email= "abc@uhk.cz";
        String password = "Passwd_123!";
        // Execution
        ResponseEntity<?> responseEntity = userService.login(email,password);

        // Verification
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertThat(responseEntity.getBody(), is("Uživatel nenalezen"));
    }

    @Test
    void changePassword_Hds() {
        log.info("Creating a user and changing his password. The old password should not match with the hashed version of the current password");
        // Data preparation
        String email = "home.simpson@uhk.cz";
        String nickname = "homer1";
        String password = "Passwd_123!";
        String newPassword = "NeWpAsSw0rD";
        LocalDate birthdate = LocalDate.of(2000, 1, 1);
        int height = 178;
        int weight = 70;
        Sex sex = Sex.MALE;
        BodyType bodyType = BodyType.AVERAGE;

        UserDTO dto = new UserDTO(birthdate, email, nickname, password, sex, weight, height, bodyType, 0, 0);

        // Execution
        userService.register(dto);
        ResponseEntity<?> responseEntity = userService.login(email,password);
        String token = Objects.requireNonNull(responseEntity.getBody()).toString();
        userService.changePassword(token,password,newPassword);
        UserStats userStats = (UserStats) userService.getUserData(token).getBody();
        assert userStats != null;

        //Verification
        assertTrue(passwordEncoder.matches(newPassword,userStats.getUser().getPassword()));
        assertFalse(passwordEncoder.matches(password,userStats.getUser().getPassword()));

    }

    @Test
    void changePassword_NewPasswordViolation() {
        log.info("Creating a user and changing his password with an invalid password. The password should not change");
        // Data preparation
        String email = "home.simpson@uhk.cz";
        String nickname = "homer1";
        String password = "Passwd_123!";
        String newPassword = "123";
        LocalDate birthdate = LocalDate.of(2000, 1, 1);
        int height = 178;
        int weight = 70;
        Sex sex = Sex.MALE;
        BodyType bodyType = BodyType.AVERAGE;

        UserDTO dto = new UserDTO(birthdate, email, nickname, password, sex, weight, height, bodyType, 0, 0);

        // Execution
        userService.register(dto);
        ResponseEntity<?> responseEntity1 = userService.login(email,password);
        String token = Objects.requireNonNull(responseEntity1.getBody()).toString();
        ResponseEntity<?> responseEntity2 = userService.changePassword(token,password,newPassword);
        UserStats userStats = (UserStats) userService.getUserData(token).getBody();
        assert userStats != null;

        //Verification
        assertThat(responseEntity2, notNullValue());
        assertThat(responseEntity2.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertThat(responseEntity2.getBody(), is("Nové heslo musí obsahovat alespoň 8 znaků"));

        assertFalse(passwordEncoder.matches(newPassword,userStats.getUser().getPassword()));
        assertTrue(passwordEncoder.matches(password,userStats.getUser().getPassword()));
    }

    @Test
    void changePassword_WrongOldPassword() {
        log.info("Creating a user and changing trying to change password with an invalid old password. The password should not change");
        // Data preparation
        String email = "home.simpson@uhk.cz";
        String nickname = "homer1";
        String password = "Passwd_123!";
        String wrongPassword = "wrongPassword123";
        String newPassword = "Password999";
        LocalDate birthdate = LocalDate.of(2000, 1, 1);
        int height = 178;
        int weight = 70;
        Sex sex = Sex.MALE;
        BodyType bodyType = BodyType.AVERAGE;

        UserDTO dto = new UserDTO(birthdate, email, nickname, password, sex, weight, height, bodyType, 0, 0);

        // Execution
        userService.register(dto);
        ResponseEntity<?> responseEntity1 = userService.login(email,password);
        String token = Objects.requireNonNull(responseEntity1.getBody()).toString();
        ResponseEntity<?> responseEntity2 = userService.changePassword(token,wrongPassword,newPassword);
        UserStats userStats = (UserStats) userService.getUserData(token).getBody();
        assert userStats != null;

        //Verification
        assertThat(responseEntity2, notNullValue());
        assertThat(responseEntity2.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertThat(responseEntity2.getBody(), is("Špatné aktuální heslo"));

        assertFalse(passwordEncoder.matches(newPassword,userStats.getUser().getPassword()));
        assertTrue(passwordEncoder.matches(password,userStats.getUser().getPassword()));
    }


    @Test
    void registerExample_Hds() {
        log.info("Creating user entity and saving it to the database. Expected result is 1 record in database, containing provided values");
        // Checking for an empty database
        assertThat(userRepository.findAll(), empty());

        // Data preparation
        String email = "home.simpson@uhk.cz";
        String nickname = "homer1";
        String password = "Passwd_123!";
        LocalDate birthdate = LocalDate.of(2000, 1, 1);
        int height = 178;
        int weight = 70;
        Sex sex = Sex.MALE;
        BodyType bodyType = BodyType.AVERAGE;

        UserDTO dto = new UserDTO(birthdate, email, nickname, password, sex, weight, height, bodyType, 0, 0);

        // Execution
        ResponseEntity<?> responseEntity = userService.register(dto);

        // Verification
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(responseEntity.getBody(), equalTo("Účet byl úspěšně zaregistrován"));

        // Verify status/database record
        List<User> users = userRepository.findAll();
        assertThat(users, hasSize(1));

        User user = users.get(0);
        assertThat(user.getId(), notNullValue());
        assertThat(user.getId(), greaterThan(0L));
        assertThat(user.getDateOfBirth(), equalTo(birthdate));
        assertThat(user.getEmail(), equalTo(email));
        assertThat(user.getHeight(), equalTo((double) height));
        assertThat(user.getNickname(), equalTo(nickname));
        assertThat(user.getPassword(), notNullValue());
        assertThat(user.getPassword().length(), greaterThan(50));
        assertThat(user.getWeight(), equalTo((double) weight));
        assertThat(user.getBodyType(), is(bodyType));
        assertThat(user.getSex(), is(sex));
        assertThat(user.getToken(), nullValue());
    }

    @Test
    void register_ValidationViolation() {
        log.info("Creating new user with attributes, that violates the rules. Expected output is error message containing all the violations");

        // Checking for an empty database
        assertThat(userRepository.findAll(), empty());

        // Data preparation
        String email = "abc?!;";
        String nickname = "123!()<>{}\\|@";
        String password = "A";
        LocalDate birthdate = LocalDate.now().plusYears(1L);
        int height = 54;
        int weight = 4;
        Sex sex = null;
        BodyType bodyType = BodyType.AVERAGE;

        UserDTO dto = new UserDTO(birthdate, email, nickname, password, sex, weight, height, bodyType, 0, 0);

        // Execution
        ResponseEntity<?> responseEntity = userService.register(dto);

        // Verification
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertThat(responseEntity.getBody(), instanceOf(String.class));
        String error = (String) responseEntity.getBody();
        assertThat(error, containsString("Prosím, vyplňte všechna pole pro registraci"));
        assertThat(error, containsString("Prosím, zadejte platnou e-mailovou adresu"));
        assertThat(error, containsString("Přezdívka musí být 5 až 20 znaků a nesmí obsahovat speciální znaky"));
        assertThat(error, containsString("Aplikace je určená osobám starším 18 let"));
        assertThat(error, containsString("Heslo musí mít alespoň 8 znaků"));
        assertThat(error, containsString("Prosím zadejte platnou váhu"));
        assertThat(error, containsString("Prosím zadejte platnou výšku"));

        assertThat(userRepository.findAll(), empty());
    }

    @Test
    void register_SaveUserError() {
        log.info("Simulating error while saving user to database. Expected result is an error and empty database");

        // Mock settings
        when(userRepositoryMock.save(any(User.class)))
                .thenThrow(new RuntimeException("Test save user error"));

        // Data preparation
        String email = "home.simpson@uhk.cz";
        String nickname = "homer1";
        String password = "Passwd_123!";
        LocalDate birthdate = LocalDate.of(2000, 1, 1);
        int height = 178;
        int weight = 70;
        Sex sex = Sex.MALE;
        BodyType bodyType = BodyType.AVERAGE;


        UserDTO dto = new UserDTO(birthdate, email, nickname, password, sex, weight, height, bodyType, 0, 0);

        // Execution
        UserService userServiceTest = new UserService(
                userRepositoryMock,
                userStatsRepository,
                jwtUtils,
                passwordEncoder,
                messageSource
        );
        ResponseEntity<?> responseEntity = userServiceTest.register(dto);

        // Verification
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertThat(responseEntity.getBody(), instanceOf(String.class));
        assertThat(responseEntity.getBody(), is("Nepodařilo se uložit Uživatele do databáze"));

        assertThat(userRepository.findAll(), empty());

    }

    @Test
    void getUserData_Hds() {
        log.info("Loading user data from database. Output of this method is userStats entity, containing same data as previously defined for the user");
        assertThat(userRepository.findAll(), empty());

        // Data preparation
        String email = "home.simpson@uhk.cz";
        String nickname = "homer1";
        String password = "Passwd_123!";
        LocalDate birthdate = LocalDate.of(2000, 1, 1);
        double height = 178;
        double weight = 70;
        Sex sex = Sex.MALE;
        BodyType bodyType = BodyType.AVERAGE;

        UserDTO dto = new UserDTO(birthdate, email, nickname, password, sex, weight, height, bodyType, 0, 0);

        // Execution
        userService.register(dto);

        ResponseEntity<?> responseEntity1 = userService.login(email,password);
        String token = (String) responseEntity1.getBody();
        ResponseEntity<?> responseEntity2 = userService.getUserData(token);

        // Verification
        assertThat(responseEntity2, notNullValue());
        assertThat(responseEntity2.getStatusCode(), equalTo(HttpStatus.OK));
        UserStats userStats = (UserStats) responseEntity2.getBody();
        assert userStats != null;
        assertThat(userStats.getUser().getEmail(), is(email));
        assertThat(userStats.getUser().getNickname(), is(nickname));
        assertTrue(passwordEncoder.matches(password,userStats.getUser().getPassword()));
        assertThat(userStats.getUser().getDateOfBirth(), is(birthdate));
        assertThat(userStats.getUser().getWeight(), is(weight));
        assertThat(userStats.getUser().getHeight(), is(height));
        assertThat(userStats.getUser().getSex(), is(sex));
        assertThat(userStats.getUser().getBodyType(), is(bodyType));
    }

    @Test
    void generateUserStats_Hds() {
        log.info("Registering a user, which automatically generates his userStats. Expected result is saved userStats entity with all userStats data being calculated according to their formulas");
        log.info("This method is only accessed threw register and updateData methods and the input data are already checked in these methods, so there is not any room for wrong input data");
        assertThat(userRepository.findAll(), empty());
        assertThat(userStatsRepository.findAll(), empty());

        // Data preparation
        String email = "home.simpson@uhk.cz";
        String nickname = "homer1";
        String password = "Passwd_123!";
        LocalDate birthdate = LocalDate.of(2000, 1, 1);
        int height = 178;
        int weight = 70;
        Sex sex = Sex.MALE;
        BodyType bodyType = BodyType.AVERAGE;

        UserDTO dto = new UserDTO(birthdate, email, nickname, password, sex, weight, height, bodyType, 0, 0);

        // Execution
        ResponseEntity<?> responseEntity = userService.register(dto);

        double bmi = (double) weight / ((double) height / 100 * (double) height / 100);
        double waterIntake = weight * 0.033;
        int userAge = Period.between(birthdate, LocalDate.now()).getYears();
        double bmr = 10 * weight + 6.25 * height - 5 * userAge + 5;

        // Verification
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(responseEntity.getBody(), equalTo("Účet byl úspěšně zaregistrován"));
        List<UserStats> userStats = userStatsRepository.findAll();
        UserStats userStatsEntity = userStats.get(0);
        assertThat(userStats, hasSize(1));
        assertThat(userStatsEntity.getBmi(), closeTo(bmi, 0.01));
        assertThat(userStatsEntity.getWaterintake(), closeTo(waterIntake, 0.01));
        assertThat(userStatsEntity.getBmr(), closeTo(bmr, 0.01));
    }

    @Test
    void updateData_Hds() {
        log.info("Updating data of existing user, estimated output is updated user data in database.");
        assertThat(userRepository.findAll(), empty());

        // Data preparation
        String email = "home.simpson@uhk.cz";
        String nickname = "homer1";
        String password = "Passwd_123!";
        LocalDate birthdate = LocalDate.of(2000, 1, 1);
        int height = 178;
        int weight = 70;
        Sex sex = Sex.MALE;
        BodyType bodyType = BodyType.AVERAGE;

        UserDTO dto = new UserDTO(birthdate, email, nickname, password, sex, weight, height, bodyType, 0, 0);

        // Execution
        userService.register(dto);
        List<User> users = userRepository.findAll();
        User user = users.get(0);
        long id = user.getId();
        String newNick = "homer2";
        String newEmail = "abc@gmail.com";
        user.setNickname(newNick);
        user.setEmail(newEmail);
        ResponseEntity<?> responseEntity = userService.updateData(user);

        // Verification
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(responseEntity.getBody(), equalTo("Uživatelská data byla aktualizována"));

        User userEntity = userRepository.findUserById(id);
        assertThat(userEntity.getNickname(), is(newNick));
        assertThat(userEntity.getEmail(), is(newEmail));
    }

    @Test
    void updateData_PasswordViolation() {
        log.info("Updating user data with changing users password. Estimated output is not being able to change password, because this action has its owm secure method");
        assertThat(userRepository.findAll(), empty());

        // Data preparation
        String email = "home.simpson@uhk.cz";
        String nickname = "homer1";
        String password = "Passwd_123!";
        LocalDate birthdate = LocalDate.of(2000, 1, 1);
        int height = 178;
        int weight = 70;
        Sex sex = Sex.MALE;
        BodyType bodyType = BodyType.AVERAGE;

        UserDTO dto = new UserDTO(birthdate, email, nickname, password, sex, weight, height, bodyType, 0, 0);

        // Execution
        userService.register(dto);
        List<User> users = userRepository.findAll();
        User user = users.get(0);
        long id = user.getId();
        String newPassword = "12345678";
        user.setPassword(newPassword);
        ResponseEntity<?> responseEntity = userService.updateData(user);

        // Verification
        User userEntity = userRepository.findUserById(id);

        assertTrue(passwordEncoder.matches(password,userEntity.getPassword()));
        assertFalse(passwordEncoder.matches(newPassword, userEntity.getPassword()));
    }
}