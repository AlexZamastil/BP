package cz.uhk.fim.project.bakalarka.controller;

import cz.uhk.fim.project.bakalarka.DTO.ExerciseDTO;
import cz.uhk.fim.project.bakalarka.service.ExerciseService;
import cz.uhk.fim.project.bakalarka.util.AuthorizationCheck;
import cz.uhk.fim.project.bakalarka.util.MessageHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Controller class for handling exercise-related endpoints.
 *
 * @author Alex Zamastil
 */
@RestController
@RequestMapping("/api")

public class ExerciseController {
    private final ExerciseService exerciseService;
    MessageHandler<String> messageHandler = new MessageHandler<>();

    @Autowired
    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    /**
     * Endpoint for adding a new exercise
     *
     * @param exerciseRequest = the DTO containing the exercise data
     * @param imageData       = data of the exercise image
     * @param request         = HttpServletRequest that contains headers, authorization header is needed for this request
     * @return ResponseEntity with the result of the operation.
     */

    @PostMapping(value = "exercise/addExercise", consumes = "multipart/form-data")
    public ResponseEntity<?> addExercise(
            @RequestPart(name = "exerciseRequest") ExerciseDTO exerciseRequest,
            @RequestPart(name = "imageData") MultipartFile imageData,
            HttpServletRequest request
    ) {
        if (!AuthorizationCheck.hasAuthorization(request)) return messageHandler.error("Missing authorization");
        return exerciseService.addNewExercise(exerciseRequest, imageData);
    }

    /**
     * Endpoint for retrieving exercise details by ID.
     *
     * @param id The ID of the exercise to retrieve.
     * @return ResponseEntity with the exercise details. (retrieving image data has its own endpoint)
     */
    @GetMapping(value = "exercise/getExercise/{id}")
    public ResponseEntity<?> getExercise(@PathVariable Long id) {
        return exerciseService.getExercise(id);
    }

    /**
     * Endpoint for retrieving the picture of an exercise by ID.
     *
     * @param id The ID of the exercise to retrieve the picture for.
     * @return ResponseEntity with the exercise picture.
     */
    @GetMapping(value = "exercise/getExercise/picture/{id}")
    public ResponseEntity<?> getExercisePicture(@PathVariable Long id) {
        return exerciseService.getExercisePicture(id);
    }
}

