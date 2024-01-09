package cz.uhk.fim.project.bakalarka.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import cz.uhk.fim.project.bakalarka.DAO.*;
import cz.uhk.fim.project.bakalarka.model.*;
import cz.uhk.fim.project.bakalarka.request.ExerciseRequest;
import cz.uhk.fim.project.bakalarka.service.ExerciseService;
import cz.uhk.fim.project.bakalarka.util.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

@RestController
@RequestMapping("/api")

public class ExerciseController {
    private final ExerciseService exerciseService;
    private final ExerciseRepository exerciseRepository;

    private final GymWorkoutRepository gymWorkoutRepository;
    private final RunRepository runRepository;
    private final SwimmingRepository swimmingRepository;
    private final TagRepository tagRepository;



    @Autowired
    public ExerciseController(ExerciseService exerciseService, ExerciseRepository exerciseRepository, GymWorkoutRepository gymWorkoutRepository, RunRepository runRepository, SwimmingRepository swimmingRepository, TagRepository tagRepository) {
        this.exerciseService = exerciseService;
        this.exerciseRepository = exerciseRepository;
        this.gymWorkoutRepository = gymWorkoutRepository;
        this.runRepository = runRepository;
        this.swimmingRepository = swimmingRepository;
        this.tagRepository = tagRepository;
    }

    @PostMapping(value = "privileged/addExercise", consumes = "multipart/form-data")
    public ResponseEntity<?> addExercise(
            @RequestPart(name = "exerciseRequest") ExerciseRequest exerciseRequest,
            @RequestPart(name = "imageData", required = false) MultipartFile imageData
    ){
        System.out.println(imageData);
        if (imageData != null) {
            if (exerciseRequest.getCategory_style() != null) {
                return exerciseService.addNewExercise(exerciseRequest.getName(),
                        exerciseRequest.getDescription(),
                        exerciseRequest.getName_eng(),
                        exerciseRequest.getDescription_eng(),
                        exerciseRequest.getType(),
                        exerciseRequest.getCategory_style(),
                        exerciseRequest.getLength(),
                        exerciseRequest.getTags(),
                        imageData);
            } else {
                return exerciseService.addNewExercise(exerciseRequest.getName(),
                        exerciseRequest.getDescription(),
                        exerciseRequest.getName_eng(),
                        exerciseRequest.getDescription_eng(),
                        exerciseRequest.getRepetitions(),
                        exerciseRequest.getSeries(),
                        exerciseRequest.getTags(),
                        imageData);
            }
        } else {
            if (exerciseRequest.getCategory_style() != null) {
                return exerciseService.addNewExercise(exerciseRequest.getName(),
                        exerciseRequest.getDescription(),
                        exerciseRequest.getName_eng(),
                        exerciseRequest.getDescription_eng(),
                        exerciseRequest.getType(),
                        exerciseRequest.getCategory_style(),
                        exerciseRequest.getLength(),
                        exerciseRequest.getTags());
            } else {
                return exerciseService.addNewExercise(exerciseRequest.getName(),
                        exerciseRequest.getDescription(),
                        exerciseRequest.getName_eng(),
                        exerciseRequest.getDescription_eng(),
                        exerciseRequest.getRepetitions(),
                        exerciseRequest.getSeries(),
                        exerciseRequest.getTags());
            }
        }
    }
    @GetMapping(value = "/nonauthorized/getExercise/{id}")
    public ResponseEntity<?> getExercise(@PathVariable Long id) throws JsonProcessingException {
        Optional<Exercise> e = exerciseRepository.findExerciseById(id);
        System.out.println(e + " EXERCISE");

        if (e.isPresent()){
            Exercise exercise = e.get();
            System.out.println("b");
            Optional<GymWorkout> g = gymWorkoutRepository.findByExercise(exercise);
            Optional<Run> r = runRepository.findByExercise(exercise);
            Optional<Swimming> s = swimmingRepository.findByExercise(exercise);
            System.out.println("AA");
            Set<Tag> tags = exerciseRepository.findTagsByExerciseId(id);
            List<String> tagList = new ArrayList<>();
            for (Tag t : tags){
                tagList.add(t.getText());
            }
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonList = objectMapper.writeValueAsString(tagList);
            System.out.println(jsonList);

            if(g.isPresent()){
                GymWorkout gymWorkout = g.get();
                ExerciseRequest exerciseRequest = new ExerciseRequest(
                        exercise.getName(),
                        exercise.getName_eng(),
                        exercise.getDescription(),
                        exercise.getDescription_eng(),
                        gymWorkout.getRepetitions(),
                        gymWorkout.getSeries(),
                        jsonList
                        );
                return MessageHandler.success(exerciseRequest.toString());
            } else if (r.isPresent()){
                Run run = r.get();
                ExerciseRequest exerciseRequest = new ExerciseRequest(
                        exercise.getName(),
                        exercise.getName_eng(),
                        exercise.getDescription(),
                        exercise.getDescription_eng(),
                        run.getRuncategory().toString(),
                        run.getLenglhinmeters(),
                        jsonList
                        );
                return MessageHandler.success(exerciseRequest.toString());
            } else if (s.isPresent()){
                Swimming swimming = s.get();
                ExerciseRequest exerciseRequest = new ExerciseRequest(
                        exercise.getName(),
                        exercise.getName_eng(),
                        exercise.getDescription(),
                        exercise.getDescription_eng(),
                        swimming.getSwimmingstyle().toString(),
                        swimming.getLenglhinmeters(),
                        jsonList);
                return MessageHandler.success(exerciseRequest.toString());
            }

        }
        return MessageHandler.error("invalid exercise ID");
    }
}
