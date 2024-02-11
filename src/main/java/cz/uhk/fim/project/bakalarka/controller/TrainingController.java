package cz.uhk.fim.project.bakalarka.controller;

import cz.uhk.fim.project.bakalarka.DAO.UserRepository;
import cz.uhk.fim.project.bakalarka.DAO.UserStatsRepository;
import cz.uhk.fim.project.bakalarka.request.CreateTrainingRequest;
import cz.uhk.fim.project.bakalarka.service.TrainingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class TrainingController {

    private final TrainingService trainingService;
    private final UserRepository userRepository;
    private final UserStatsRepository userStatsRepository;
    @Autowired
    public TrainingController(TrainingService trainingService, UserRepository userRepository, UserStatsRepository userStatsRepository) {
        this.trainingService = trainingService;
        this.userRepository = userRepository;
        this.userStatsRepository = userStatsRepository;
    }

    @PostMapping(value = "authorized/generateTraining", consumes = {"application/json"})
    public ResponseEntity<?> generateTraining(@RequestBody CreateTrainingRequest createTrainingRequest, HttpServletRequest httpServletRequest) throws Exception {
        trainingService.trainModel();
        return trainingService.generateTraining(createTrainingRequest, httpServletRequest);
    }

    @GetMapping(value = "authorized/hasActiveTraining/{id}")
    public boolean hasActiveTraining(@PathVariable long id){
        return trainingService.hasActiveTraining(id);
    }

    @PostMapping(value = "unauthorized/trainJ48")
    public void trainJ48() throws Exception {trainingService.trainModel();}
}
