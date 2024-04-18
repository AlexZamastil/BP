package cz.uhk.fim.project.bakalarka.service;

import cz.uhk.fim.project.bakalarka.DAO.UserRepository;
import cz.uhk.fim.project.bakalarka.DAO.UserStatsRepository;
import cz.uhk.fim.project.bakalarka.DTO.UserDTO;
import cz.uhk.fim.project.bakalarka.enumerations.BodyType;
import cz.uhk.fim.project.bakalarka.enumerations.Sex;
import cz.uhk.fim.project.bakalarka.model.User;
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
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private UserStatsRepository userStatsRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MessageSource messageSource;


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
        log.info("");

        userStatsRepository.deleteAll();
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
        String nickname = "homee";
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
        List<User> users = userRepository.findAll();// FIXME Možno testovat načtení očekávaného uživatele dle emailu, ale je vhodné ověřit, že se v databázi nevyskytuje žádný jiný záznam
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
    void registerExample_ValidationViolation() {
        log.info("");// FIXME - co se testuje a ocekávaný výsledek

        // Checking for an empty database
        assertThat(userRepository.findAll(), empty());

        // Data preparation
        String email = "abc?!;";
        String nickname = "123!()<>{}\\|@";
        String password = "A";
        LocalDate birthdate = LocalDate.now().plusYears(1L);
        int height = 178;
        int weight = 70;
        Sex sex = Sex.MALE;
        BodyType bodyType = BodyType.AVERAGE;

        UserDTO dto = new UserDTO(birthdate, email, nickname, password, sex, weight, height, bodyType, 0, 0);

        // Execution
        ResponseEntity<?> responseEntity = userService.register(dto);

        // Verification
//        FIXME Postrádám zde informaci o dalších nevalidních či neuvedených povinných hodnotách - jsou-li
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertThat(responseEntity.getBody(), is("Prosím, zadejte platnou e-mailovou adresu"));

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
        String nickname = "homee";
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
        assertThat(responseEntity.getBody(), is("Nepodařilo se uložit Uživatele do databáze"));
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