package cz.uhk.fim.project.bakalarka.controller;


import cz.uhk.fim.project.bakalarka.request.CreateTrainingRequest;
import cz.uhk.fim.project.bakalarka.service.TrainingService;
import cz.uhk.fim.project.bakalarka.util.MessageHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class trainingController {

    private final TrainingService trainingService;
    @Autowired
    public trainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }
/*
    @PostMapping(value = "authorized/generateTraining", consumes = {"aplication/json"})
    public ResponseEntity<?> generateTraining(CreateTrainingRequest createTrainingRequest, HttpServletRequest httpServletRequest){
        return trainingService.;
    }

 */
}
