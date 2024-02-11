package cz.uhk.fim.project.bakalarka.controller;

import cz.uhk.fim.project.bakalarka.request.ExerciseRequest;
import cz.uhk.fim.project.bakalarka.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/api")

public class ExerciseController {
    private final ExerciseService exerciseService;

    @Autowired
    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @PostMapping(value = "privileged/addExercise", consumes = "multipart/form-data")
    public ResponseEntity<?> addExercise(
            @RequestPart(name = "exerciseRequest") ExerciseRequest exerciseRequest,
            @RequestPart(name = "imageData", required = false) MultipartFile imageData
    ) {
        return exerciseService.addNewExercise(exerciseRequest, imageData);
    }

    @GetMapping(value = "/unauthorized/getExercise/{id}")
    public ResponseEntity<?> getExercise(@PathVariable Long id) throws IOException {
        return exerciseService.getExercise(id);
    }
}

