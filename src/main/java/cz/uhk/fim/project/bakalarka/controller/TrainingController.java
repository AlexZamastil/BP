package cz.uhk.fim.project.bakalarka.controller;


import cz.uhk.fim.project.bakalarka.request.CreateTrainingRequest;
import cz.uhk.fim.project.bakalarka.service.TrainingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import weka.classifiers.trees.J48;

@RestController
@RequestMapping("/api")
public class TrainingController {

    private final TrainingService trainingService;
    private J48 dataModel;
    @Autowired
    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @PostMapping(value = "authorized/generateTraining", consumes = {"application/json"})
    public ResponseEntity<?> generateTraining(@RequestBody CreateTrainingRequest createTrainingRequest, HttpServletRequest httpServletRequest){
        /*
        System.out.println(createTrainingRequest);
        trainingService.createTraining(
                createTrainingRequest.getStartDay(),
                createTrainingRequest.getRaceDay(),
                createTrainingRequest.getGoal(),
                createTrainingRequest.getLengthOfRaceInMeters(),
                createTrainingRequest.getWantedTime(),
                createTrainingRequest.getActualRunLength(),
                createTrainingRequest.getActualTime(),
                httpServletRequest);

         */
        System.out.println("A");
        return trainingService.generateTrainingPlan(80, 180, 24.7, "M", "Average", 4, 90, 5000, 6.5, 8000, 6.0, "Run", "Average");


    }

    @GetMapping(value = "authorized/hasActiveTraining/{id}")
    public boolean hasActiveTraining(@PathVariable long id){
        return trainingService.hasActiveTraining(id);
    }

    @PostMapping(value = "nonauthorized/trainJ48")
    public void trainJ48() throws Exception {trainingService.trainModel();}

    @PostMapping(value = "nonauthorized/test1234")
    public ResponseEntity<?> test1234(){
        return trainingService.generateTrainingPlan(80, 180, 24.7, "M", "Average", 4, 120, 5000, 6.5, 10000, 6.5, "Run", "Average");

    }

}
