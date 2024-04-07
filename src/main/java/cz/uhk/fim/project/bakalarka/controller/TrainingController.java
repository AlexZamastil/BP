package cz.uhk.fim.project.bakalarka.controller;

import cz.uhk.fim.project.bakalarka.DTO.CreateTrainingDTO;
import cz.uhk.fim.project.bakalarka.service.TrainingService;
import cz.uhk.fim.project.bakalarka.util.AuthorizationCheck;
import cz.uhk.fim.project.bakalarka.util.MessageHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class TrainingController {
    MessageHandler<String> messageHandler;
    private final TrainingService trainingService;
    @Autowired
    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @PostMapping(value = "training/generateTraining", consumes = {"application/json"})
    public ResponseEntity<?> generateTraining(@RequestBody CreateTrainingDTO createTrainingRequest, HttpServletRequest request) throws Exception {
        if (!AuthorizationCheck.hasAuthorization(request)) return messageHandler.error("Missing authorization");
        trainingService.trainModel();
        return trainingService.generateTraining(createTrainingRequest, request);
    }

    @GetMapping(value = "training/hasActiveTraining/{id}")
    public boolean hasActiveTraining(@PathVariable long id){
        return trainingService.hasActiveTraining(id);
    }

    @GetMapping(value = "training/getTrainings")
    public ResponseEntity<?> getTrainings(HttpServletRequest request){
        if (!AuthorizationCheck.hasAuthorization(request)) return messageHandler.error("Missing authorization");
        return trainingService.getTrainings(request.getHeader("Authorization"));}

    @PostMapping(value = "training/trainJ48")
    public void trainJ48() throws Exception {trainingService.trainModel();}
}
