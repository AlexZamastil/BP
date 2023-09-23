package cz.uhk.fim.project.bakalarka.controller;

import cz.uhk.fim.project.bakalarka.request.AddExerciseRequest;
import cz.uhk.fim.project.bakalarka.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")

public class ExerciseController {
    private final ExerciseService exerciseService;

    @Autowired
    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @PostMapping(value = "privileged/addExercise", consumes = {"Application/json"})
    public ResponseEntity<?> addExercise(@RequestBody AddExerciseRequest addExerciseRequest) {
        if (addExerciseRequest.getImageData() != null) {
            if (addExerciseRequest.getCategory_style() != null) {
                return exerciseService.addNewExercise(addExerciseRequest.getName(),
                        addExerciseRequest.getDescription(),
                        addExerciseRequest.getName_eng(),
                        addExerciseRequest.getDescription_eng(),
                        addExerciseRequest.getType(),
                        addExerciseRequest.getCategory_style(),
                        addExerciseRequest.getLength(),
                        addExerciseRequest.getTags(),
                        addExerciseRequest.getImageData());
            } else {
                return exerciseService.addNewExercise(addExerciseRequest.getName(),
                        addExerciseRequest.getDescription(),
                        addExerciseRequest.getName_eng(),
                        addExerciseRequest.getDescription_eng(),
                        addExerciseRequest.getRepetitions(),
                        addExerciseRequest.getSeries(),
                        addExerciseRequest.getTags(),
                        addExerciseRequest.getImageData());
            }
        } else {
            if (addExerciseRequest.getCategory_style() != null) {
                return exerciseService.addNewExercise(addExerciseRequest.getName(),
                        addExerciseRequest.getDescription(),
                        addExerciseRequest.getName_eng(),
                        addExerciseRequest.getDescription_eng(),
                        addExerciseRequest.getType(),
                        addExerciseRequest.getCategory_style(),
                        addExerciseRequest.getLength(),
                        addExerciseRequest.getTags());
            } else {
                return exerciseService.addNewExercise(addExerciseRequest.getName(),
                        addExerciseRequest.getDescription(),
                        addExerciseRequest.getName_eng(),
                        addExerciseRequest.getDescription_eng(),
                        addExerciseRequest.getRepetitions(),
                        addExerciseRequest.getSeries(),
                        addExerciseRequest.getTags());
            }
        }
    }
}
