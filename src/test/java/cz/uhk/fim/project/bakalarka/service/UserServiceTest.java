package cz.uhk.fim.project.bakalarka.service;

import cz.uhk.fim.project.bakalarka.DAO.UserRepository;
import cz.uhk.fim.project.bakalarka.DAO.UserStatsRepository;
import cz.uhk.fim.project.bakalarka.DTO.UserDTO;
import cz.uhk.fim.project.bakalarka.enumerations.BodyType;
import cz.uhk.fim.project.bakalarka.enumerations.Sex;
import cz.uhk.fim.project.bakalarka.model.User;
import cz.uhk.fim.project.bakalarka.util.JWTUtils;
import cz.uhk.fim.project.bakalarka.util.MessageHandler;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.validator.routines.EmailValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
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
    @MockBean
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private UserStatsRepository userStatsRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @BeforeEach
    void setUp() {
        log.info("Resetting environment");

        userRepository.deleteAll();

        reset(userRepositoryMock);
        clearInvocations(userRepositoryMock);
    }

    @AfterEach
    void tearDown() {
        log.info("");

        userRepository.deleteAll();
    }

    @Test
    void login() {
    }

    @Test
    void changePassword() {
    }

    @Test
    void register() {
    }

    @Test
    void registerExample_Hds() {
        log.info("");// FIXME - co se testuje a ocekávaný výsledek

        // Checking for an empty database
        assertThat(userRepository.findAll(), empty());

        // Data preparation
        String email = "home.simpson@uhk.cz";
        String nickname = "home";
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
        assertThat(responseEntity.getBody(), equalTo("Account created successfully"));

        // Verify status/database record
        List<User> users = userRepository.findAll();// FIXME Možno testovat načtení očekávaného uživatele dle emailu, ale je vhodné ověřit, že se v databázi nevyskytuje žádný jiný záznam
        assertThat(users, hasSize(1));

        User user = users.get(0);
        assertThat(user.getId(), notNullValue());
        assertThat(user.getId(), greaterThan(0L));
        assertThat(user.getDateOfBirth(), equalTo(birthdate));
        assertThat(user.getEmail(), equalTo(email));
        assertThat(user.getHeight(), equalTo(0.0D));
        assertThat(user.getNickname(), equalTo(nickname));
        assertThat(user.getPassword(), notNullValue());
        assertThat(user.getPassword().length(), greaterThan(50));
        assertThat(user.getWeight(), equalTo(0.0));
        assertThat(user.getBodyType(), nullValue());
        assertThat(user.getSex(), is(sex));
        assertThat(user.getToken(), nullValue());
    }

    @Test
    void registerExample_ValidationViolation() {
        log.info("");// FIXME - co se testuje a ocekávaný výsledek

        // Checking for an empty database
        assertThat(userRepository.findAll(), empty());

        // Data preparation
        String email = "abc?!;";
        String nickname = "123!()<>{}\\|@";
        String password = "A";
        LocalDate birthdate = LocalDate.now().plusYears(1L);

        UserDTO dto = new UserDTO(birthdate, email, nickname, password, null, 0, 0, null, 0, 0);

        // Execution
        ResponseEntity<?> responseEntity = userService.register(dto);

        // Verification
//        FIXME Postrádám zde informaci o dalších nevalidních či neuvedených povinných hodnotách - jsou-li
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertThat(responseEntity.getBody(), is("The entered text is not a valid email address"));

        // Verify status/database record
        assertThat(userRepository.findAll(), empty());
    }

    @Test
    void registerExample_SaveUserError() {
        log.info("");// FIXME - co se testuje a ocekávaný výsledek

        // Mock settings
        when(userRepositoryMock.save(any(User.class)))
                .thenThrow(new RuntimeException("Test save user error"));

        // Data preparation
        String email = "home.simpson@uhk.cz";
        String nickname = "home";
        String password = "Passwd_123!";
        LocalDate birthdate = LocalDate.of(2000, 1, 1);
        Sex sex = Sex.MALE;

        UserDTO dto = new UserDTO(birthdate, email, nickname, password, sex);

        // Execution

        ResponseEntity<?> responseEntity = userService.register(dto);

        // Verification
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertThat(responseEntity.getBody(), is("An error occurred while trying to save (/register) a new user."));
    }

    @Test
    void getUserData() {
    }

    @Test
    void generateUserStats() {
    }

    @Test
    void updateData() {
    }
}