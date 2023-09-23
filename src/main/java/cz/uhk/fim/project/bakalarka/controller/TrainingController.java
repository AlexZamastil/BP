package cz.uhk.fim.project.bakalarka.controller;


import cz.uhk.fim.project.bakalarka.request.CreateTrainingRequest;
import cz.uhk.fim.project.bakalarka.service.TrainingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TrainingController {

    private final TrainingService trainingService;
    @Autowired
    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @PostMapping(value = "authorized/generateTraining", consumes = {"application/json"})
    public ResponseEntity<?> generateTraining(@RequestBody CreateTrainingRequest createTrainingRequest, HttpServletRequest httpServletRequest){
        System.out.println(createTrainingRequest);
        return trainingService.createTraining(
                createTrainingRequest.getStartDay(),
                createTrainingRequest.getRaceDay(),
                createTrainingRequest.getGoal(),
                createTrainingRequest.getLengthOfRaceInMeters(),
                createTrainingRequest.getWantedTimeInSeconds(),
                createTrainingRequest.getActualRunLength(),
                createTrainingRequest.getActualTimeInSecond(),
                httpServletRequest);
    }


}
