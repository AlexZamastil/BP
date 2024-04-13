package cz.uhk.fim.project.bakalarka.controller;

import cz.uhk.fim.project.bakalarka.DTO.CreateTrainingDTO;
import cz.uhk.fim.project.bakalarka.service.TrainingService;
import cz.uhk.fim.project.bakalarka.util.AuthorizationCheck;
import cz.uhk.fim.project.bakalarka.util.MessageHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for handling training-related endpoints.
 *
 * @author Alex Zamastil
 */
@RestController
@RequestMapping("/api")
public class TrainingController {
    MessageHandler<String> messageHandler;
    private final TrainingService trainingService;

    @Autowired
    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    /**
     * Endpoint for generating a new training.
     *
     * @param createTrainingRequest The CreateTrainingDTO containing training details.
     * @param request               The HttpServletRequest for authorization check.
     * @return ResponseEntity with the result of the operation.
     * @throws Exception if an error occurs during reading, evaluating or cross-validating data
     */
    @PostMapping(value = "training/generateTraining", consumes = {"application/json"})
    public ResponseEntity<?> generateTraining(@RequestBody CreateTrainingDTO createTrainingRequest, HttpServletRequest request) throws Exception {
        if (!AuthorizationCheck.hasAuthorization(request)) return messageHandler.error("Missing authorization");
        trainingService.trainModel();
        return trainingService.generateTraining(createTrainingRequest, request);
    }

    /**
     * Endpoint for checking if a user has an active training.
     *
     * @param id The ID of the user to check for active training.
     * @return ResponseEntity indicating if the user has an active training. It returns either ID of current training or "no"
     */
    @GetMapping(value = "training/hasActiveTraining/{id}")
    public ResponseEntity<?> hasActiveTraining(@PathVariable long id) {
        return trainingService.hasActiveTraining(id);
    }

    /**
     * Endpoint for retrieving the active training of a user.
     *
     * @param id The ID of the user to retrieve the active training for.
     * @return ResponseEntity with the active training of the user. Cannot be called accessed without user having an active training because of frontend logic.
     */
    @GetMapping(value = "training/getActiveTraining/{id}")
    public ResponseEntity<?> getActiveTraining(@PathVariable long id) {
        return trainingService.getActiveTraining(id);
    }

    /**
     * Endpoint for retrieving trainings.
     *
     * @param request The HttpServletRequest for authorization check and for obtaining user ID from token.
     * @return ResponseEntity with all trainings of the user - current and past.
     */
    @GetMapping(value = "training/getTrainings")
    public ResponseEntity<?> getTrainings(HttpServletRequest request) {
        if (!AuthorizationCheck.hasAuthorization(request)) return messageHandler.error("Missing authorization");
        return trainingService.getTrainings(request.getHeader("Authorization"));
    }

    /**
     * Endpoint for deleting a training by ID.
     *
     * @param id      The ID of the training to delete.
     * @param request The HttpServletRequest for authorization check.
     * @return ResponseEntity with the result of the operation.
     */
    @DeleteMapping(value = "training/deleteTraining/{id}")
    public ResponseEntity<?> deleteTraining(@PathVariable long id, HttpServletRequest request) {
        if (!AuthorizationCheck.hasAuthorization(request)) return messageHandler.error("Missing authorization");
        return trainingService.deleteTraining(id);
    }
}
