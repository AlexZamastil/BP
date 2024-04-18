
package cz.uhk.fim.project.bakalarka.service;

import cz.uhk.fim.project.bakalarka.BakalarkaJavaApplication;
import cz.uhk.fim.project.bakalarka.DAO.TrainingRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

@Log4j2
@SpringBootTest(classes = BakalarkaJavaApplication.class)
@WebAppConfiguration
class TrainingTest {

    @Autowired
    private TrainingService trainingService;

    @MockBean
    private TrainingRepository trainingRepository;

     @Test
     void test(){
         log.info("123");
     }

    @Test
    void testTrainingService() throws Exception {
        assertNotNull(trainingService);


        trainingService.trainModel();

        verify(trainingRepository);
    }
}

