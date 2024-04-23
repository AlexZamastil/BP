
package cz.uhk.fim.project.bakalarka.service;

import cz.uhk.fim.project.bakalarka.BakalarkaJavaApplication;
import cz.uhk.fim.project.bakalarka.DAO.TrainingRepository;
import cz.uhk.fim.project.bakalarka.enumerations.Type;
import cz.uhk.fim.project.bakalarka.model.User;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.mockito.Mockito.mock;


@Log4j2
@SpringBootTest(classes = BakalarkaJavaApplication.class)
@WebAppConfiguration
class TrainingTest {
    private final TrainingRepository trainingRepositoryMock = mock(TrainingRepository.class);


    @Autowired
    private TrainingService trainingService;

    @Autowired
    private TrainingRepository trainingRepository;

     @Test
     void getTrainings_Hds(){
         log.info("Getting information about all trainings.");
         // Checking for empty database
         assertThat(trainingRepository.findAll(), empty());
         //Data preparation
         LocalDate raceday = LocalDate.of(2024, 4, 23);
         Type type = Type.RUN;
         LocalDate startday = LocalDate.of(2024, 4, 20);
         Integer lengthInMeters = 5000;
         User user = new User();
         long id = 1;

         //CreateTrainingDTO createTrainingDTO = new CreateTrainingDTO(startday,raceday,type,lengthInMeters,)

         //trainingService.generateTraining();





     }

    @Test
    void testTrainingService() throws Exception {

    }
}

