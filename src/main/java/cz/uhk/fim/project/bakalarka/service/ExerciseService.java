package cz.uhk.fim.project.bakalarka.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.uhk.fim.project.bakalarka.DAO.*;
import cz.uhk.fim.project.bakalarka.enumerations.RunCategory;
import cz.uhk.fim.project.bakalarka.enumerations.SwimmingStyle;
import cz.uhk.fim.project.bakalarka.enumerations.Tag_Exercise;
import cz.uhk.fim.project.bakalarka.model.*;
import cz.uhk.fim.project.bakalarka.DTO.ExerciseDTO;
import cz.uhk.fim.project.bakalarka.util.MessageHandler;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final GymWorkoutRepository gymWorkoutRepository;
    private final RunRepository runRepository;
    private final SwimmingRepository swimmingRepository;

    private final PictureRepository pictureRepository;
    private final TagRepository tagRepository;
    private final MessageSource messageSource;
    MessageHandler<String> stringMessageHandler = new MessageHandler<>();
    MessageHandler<ExerciseDTO> exerciseMessageHandler = new MessageHandler<>();

    public ExerciseService(ExerciseRepository exerciseRepository, GymWorkoutRepository gymWorkoutRepository, RunRepository runRepository, SwimmingRepository swimmingRepository, PictureRepository pictureRepository, TagRepository tagRepository, MessageSource messageSource) {
        this.exerciseRepository = exerciseRepository;
        this.gymWorkoutRepository = gymWorkoutRepository;
        this.runRepository = runRepository;
        this.swimmingRepository = swimmingRepository;
        this.pictureRepository = pictureRepository;
        this.tagRepository = tagRepository;
        this.messageSource = messageSource;
    }

    public ResponseEntity<?> addNewExercise(ExerciseDTO exerciseRequest, MultipartFile multipartFile) {
        try {
            System.out.println(Arrays.toString(multipartFile.getBytes()));
        } catch (IOException e) {
            throw new RuntimeException("Unable to read image data");
        }
        switch (exerciseRequest.getType()) {
            case "RUN" -> {
                System.out.println("R");
                return addNewRunExercise(
                        exerciseRequest.getName(),
                        exerciseRequest.getDescription(),
                        exerciseRequest.getName_eng(),
                        exerciseRequest.getDescription_eng(),
                        exerciseRequest.getCategory(),
                        exerciseRequest.getLength(),
                        exerciseRequest.getTags(),
                        multipartFile);
            }
            case "GYM" -> {
                System.out.println("G");
                return addNewGymExercise(
                        exerciseRequest.getName(),
                        exerciseRequest.getDescription(),
                        exerciseRequest.getName_eng(),
                        exerciseRequest.getDescription_eng(),
                        exerciseRequest.getRepetitions(),
                        exerciseRequest.getSeries(),
                        exerciseRequest.getTags(),
                        multipartFile);
            }
            case "SWIMMING" -> {
                System.out.println("S");
                return addNewSwimmingExercise(
                        exerciseRequest.getName(),
                        exerciseRequest.getDescription(),
                        exerciseRequest.getName_eng(),
                        exerciseRequest.getDescription_eng(),
                        exerciseRequest.getStyle(),
                        exerciseRequest.getLength(),
                        exerciseRequest.getTags(),
                        multipartFile);
            }
        }
        return stringMessageHandler.error(messageSource.getMessage("error.exercise.failedToAdd", null, LocaleContextHolder.getLocale()));
    }

    public ResponseEntity<?> getExercise(long id) throws IOException {
        Optional<Exercise> e = exerciseRepository.findExerciseById(id);
        if (e.isPresent()) {
            Exercise exercise = e.get();
            Optional<GymWorkout> g = gymWorkoutRepository.findByExercise(exercise);
            Optional<Run> r = runRepository.findByExercise(exercise);
            Optional<Swimming> s = swimmingRepository.findByExercise(exercise);
            Set<Tag> tags = exerciseRepository.findTagsByExerciseId(id);
            List<String> tagList = new ArrayList<>();
            for (Tag t : tags) {
                tagList.add(t.getText());
            }
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonList = objectMapper.writeValueAsString(tagList);
            System.out.println(jsonList);

            if (g.isPresent()) {
                GymWorkout gymWorkout = g.get();
                ExerciseDTO exerciseRequest = new ExerciseDTO(
                        exercise.getName(),
                        exercise.getName_eng(),
                        exercise.getDescription(),
                        exercise.getDescription_eng(),
                        gymWorkout.getRepetitions(),
                        gymWorkout.getSeries(),
                        jsonList
                );
                return exerciseMessageHandler.success(exerciseRequest);
            } else if (r.isPresent()) {
                Run run = r.get();
                ExerciseDTO exerciseRequest = new ExerciseDTO(
                        exercise.getName(),
                        exercise.getName_eng(),
                        exercise.getDescription(),
                        exercise.getDescription_eng(),
                        run.getRuncategory().toString(),
                        run.getLenglhinmeters(),
                        jsonList
                );
                return exerciseMessageHandler.success(exerciseRequest);
            } else if (s.isPresent()) {
                Swimming swimming = s.get();
                ExerciseDTO exerciseRequest = new ExerciseDTO(
                        exercise.getName(),
                        exercise.getName_eng(),
                        exercise.getDescription(),
                        exercise.getDescription_eng(),
                        swimming.getSwimmingstyle().toString(),
                        swimming.getLenglhinmeters(),
                        jsonList);
                return exerciseMessageHandler.success(exerciseRequest);
            }

        }
        return stringMessageHandler.error(messageSource.getMessage("error.exercise.invalidID", null, LocaleContextHolder.getLocale()));
    }

    public ResponseEntity<?> getExercisePicture(long id) {
        Optional<Exercise> e = exerciseRepository.findExerciseById(id);
        if (e.isPresent()) {
            Exercise exercise = e.get();
            byte[] picture = exercise.getPicture().getPictureData();
            String pictureBytes = Base64.getEncoder().encodeToString(picture);
            return stringMessageHandler.success(pictureBytes);
        }
        return stringMessageHandler.error(messageSource.getMessage("error.exercise.invalidID", null, LocaleContextHolder.getLocale()));
    }

    public ResponseEntity<?> addNewRunExercise(String name, String description, String name_eng, String description_eng, String category, int lengthInMeters, List<String> tags, MultipartFile multipartFile) {
        Exercise exercise = createExercise(multipartFile, name, description, name_eng, description_eng, tags);
        Run run = new Run(lengthInMeters, RunCategory.valueOf(category), exercise);
        runRepository.save(run);
        return stringMessageHandler.success(messageSource.getMessage("success.exercise.added", null, LocaleContextHolder.getLocale()));
    }

    public ResponseEntity<?> addNewGymExercise(String name, String description, String name_eng, String description_eng, int repetitions, int series, List<String> tags, MultipartFile multipartFile) {
        Exercise exercise = createExercise(multipartFile, name, description, name_eng, description_eng, tags);
        GymWorkout gymWorkout = new GymWorkout(series, repetitions, exercise);
        gymWorkoutRepository.save(gymWorkout);
        return stringMessageHandler.success(messageSource.getMessage("success.exercise.added", null, LocaleContextHolder.getLocale()));
    }

    public ResponseEntity<?> addNewSwimmingExercise(String name, String description, String name_eng, String description_eng, String style, int lengthInMeters, List<String> tags, MultipartFile multipartFile) {
        Exercise exercise = createExercise(multipartFile, name, description, name_eng, description_eng, tags);
        Swimming swimming = new Swimming(lengthInMeters, SwimmingStyle.valueOf(style), exercise);
        swimmingRepository.save(swimming);
        return stringMessageHandler.success(messageSource.getMessage("success.exercise.added", null, LocaleContextHolder.getLocale()));
    }

    public void handleTags(List<String> tags, Exercise exercise) {

        for (String tag : tags) {

            tag = tag.toUpperCase();
            boolean isMatch = false;
            for (Tag_Exercise tagEnum : Tag_Exercise.values()) {

                System.out.println(tagEnum);
                System.out.println(tag);
                if (tag.equals(tagEnum.toString())) {

                    isMatch = true;
                    break;
                }
            }
            if (isMatch) {
                if (tagRepository.findTagByText(tag) == null) {
                    cz.uhk.fim.project.bakalarka.model.Tag newTag = new cz.uhk.fim.project.bakalarka.model.Tag(tag);
                    tagRepository.save(newTag);
                }
                cz.uhk.fim.project.bakalarka.model.Tag tag2 = tagRepository.findTagByText(tag);
                exerciseRepository.save(exercise);
                exercise.addTagexercise(tag2);
                tagRepository.save(tag2);
            }
        }
    }

    public byte[] handlePicture(MultipartFile f) {
        try {
            return f.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Exercise createExercise(MultipartFile multipartFile, String name, String description, String name_eng, String description_eng, List<String> tags) {
        byte[] pictureData = handlePicture(multipartFile);
        Picture picture = new Picture(pictureData);
        pictureRepository.save(picture);
        Exercise exercise = new Exercise(name, name_eng, picture, description, description_eng);
        exerciseRepository.save(exercise);
        handleTags(tags, exercise);
        return exercise;
    }
}
