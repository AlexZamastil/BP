package cz.uhk.fim.project.bakalarka.controller;

import cz.uhk.fim.project.bakalarka.DataAccessObject.ExerciseRepository;
import cz.uhk.fim.project.bakalarka.model.Exercise;
import cz.uhk.fim.project.bakalarka.request.AddExerciseRequest;
import cz.uhk.fim.project.bakalarka.service.ExerciseService;
import cz.uhk.fim.project.bakalarka.util.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")

public class ExerciseController {
    private final ExerciseService exerciseService;
    private final ExerciseRepository exerciseRepository;

    @Autowired
    public ExerciseController(ExerciseService exerciseService, ExerciseRepository exerciseRepository) {
        this.exerciseService = exerciseService;
        this.exerciseRepository = exerciseRepository;
    }

    @PostMapping(value = "privileged/addExercise", consumes = "multipart/form-data")
    public ResponseEntity<?> addExercise(
            @RequestPart(name = "addExerciseRequest") AddExerciseRequest addExerciseRequest,
            @RequestPart(name = "imageData", required = false) MultipartFile imageData
    ){
        System.out.println(imageData);
        if (imageData != null) {
            if (addExerciseRequest.getCategory_style() != null) {
                return exerciseService.addNewExercise(addExerciseRequest.getName(),
                        addExerciseRequest.getDescription(),
                        addExerciseRequest.getName_eng(),
                        addExerciseRequest.getDescription_eng(),
                        addExerciseRequest.getType(),
                        addExerciseRequest.getCategory_style(),
                        addExerciseRequest.getLength(),
                        addExerciseRequest.getTags(),
                        imageData);
            } else {
                return exerciseService.addNewExercise(addExerciseRequest.getName(),
                        addExerciseRequest.getDescription(),
                        addExerciseRequest.getName_eng(),
                        addExerciseRequest.getDescription_eng(),
                        addExerciseRequest.getRepetitions(),
                        addExerciseRequest.getSeries(),
                        addExerciseRequest.getTags(),
                        imageData);
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
    @GetMapping(value = "/nonauthorized/getExercise/{id}")
    public ResponseEntity<?> getExercise(@PathVariable long id){
        Exercise e = exerciseRepository.findExerciseById(id);
        if (e != null){
            return MessageHandler.success(e.toString());
        }
        return MessageHandler.error("invalid exercise ID");
    }
}
