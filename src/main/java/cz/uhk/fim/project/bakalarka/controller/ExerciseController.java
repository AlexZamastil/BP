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


@RestController
@RequestMapping("/api")

public class ExerciseController {
    private final ExerciseService exerciseService;

    @Autowired
    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @PostMapping(value = "exercise/addExercise", consumes = "multipart/form-data")
    public ResponseEntity<?> addExercise(
            @RequestPart(name = "exerciseRequest") ExerciseDTO exerciseRequest,
            @RequestPart(name = "imageData") MultipartFile imageData,
            HttpServletRequest request
    ) {
        if (!AuthorizationCheck.hasAuthorization(request)) return MessageHandler.error("Missing authorization");
        return exerciseService.addNewExercise(exerciseRequest, imageData);
    }

    @GetMapping(value = "exercise/getExercise/{id}")
    public ResponseEntity<?> getExercise(@PathVariable Long id) throws IOException {
        return exerciseService.getExercise(id);
    }
}

