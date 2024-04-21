package cz.uhk.fim.project.bakalarka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.uhk.fim.project.bakalarka.DAO.*;
import cz.uhk.fim.project.bakalarka.enumerations.RunCategory;
import cz.uhk.fim.project.bakalarka.enumerations.SwimmingStyle;
import cz.uhk.fim.project.bakalarka.enumerations.Tag_Exercise;
import cz.uhk.fim.project.bakalarka.model.*;
import cz.uhk.fim.project.bakalarka.DTO.ExerciseDTO;
import cz.uhk.fim.project.bakalarka.util.MessageHandler;
import io.micrometer.common.util.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
/**
 * Service class responsible for managing exercise-related operations.
 *
 * @author Alex Zamastil
 */
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
    MessageHandler<Long> longMessageHandler = new MessageHandler<>();

    public ExerciseService(ExerciseRepository exerciseRepository, GymWorkoutRepository gymWorkoutRepository, RunRepository runRepository, SwimmingRepository swimmingRepository, PictureRepository pictureRepository, TagRepository tagRepository, MessageSource messageSource) {
        this.exerciseRepository = exerciseRepository;
        this.gymWorkoutRepository = gymWorkoutRepository;
        this.runRepository = runRepository;
        this.swimmingRepository = swimmingRepository;
        this.pictureRepository = pictureRepository;
        this.tagRepository = tagRepository;
        this.messageSource = messageSource;
    }
    /**
     * Adds a new exercise based on the provided ExerciseDTO and multipart file.
     * The type of exercise (RUN, GYM, SWIMMING) determines how the exercise is created and stored.
     * Returns an appropriate ResponseEntity indicating the result of the operation.
     *
     * @param exerciseRequest Details of the exercise to be added.
     * @param multipartFile   Multipart file containing the picture associated with the exercise.
     * @return ResponseEntity containing the result of the operation.
     */

    public ResponseEntity<?> addNewExercise(ExerciseDTO exerciseRequest, MultipartFile multipartFile) {
        if(exerciseRequest == null) return stringMessageHandler.error(messageSource.getMessage("error.exercise/food.null", null, LocaleContextHolder.getLocale()));
        if (StringUtils.isBlank(exerciseRequest.getName()) ||
                StringUtils.isBlank(exerciseRequest.getName_eng()) ||
                StringUtils.isBlank(exerciseRequest.getDescription()) ||
                StringUtils.isBlank(exerciseRequest.getDescription_eng()) ||
                exerciseRequest.getCalories() == 0 ||
                multipartFile.isEmpty()
        )  return stringMessageHandler.error(messageSource.getMessage("error.exercise/food.null", null, LocaleContextHolder.getLocale()));

        switch (exerciseRequest.getType()) {
            case "RUN" -> {
                return addNewRunExercise(
                        exerciseRequest.getName(),
                        exerciseRequest.getDescription(),
                        exerciseRequest.getName_eng(),
                        exerciseRequest.getDescription_eng(),
                        exerciseRequest.getCategory(),
                        exerciseRequest.getLength(),
                        exerciseRequest.getCalories(),
                        exerciseRequest.getTags(),
                        multipartFile);
            }
            case "GYM" -> {
                return addNewGymExercise(
                        exerciseRequest.getName(),
                        exerciseRequest.getDescription(),
                        exerciseRequest.getName_eng(),
                        exerciseRequest.getDescription_eng(),
                        exerciseRequest.getRepetitions(),
                        exerciseRequest.getSeries(),
                        exerciseRequest.getCalories(),
                        exerciseRequest.getTags(),
                        multipartFile);
            }
            case "SWIMMING" -> {
                return addNewSwimmingExercise(
                        exerciseRequest.getName(),
                        exerciseRequest.getDescription(),
                        exerciseRequest.getName_eng(),
                        exerciseRequest.getDescription_eng(),
                        exerciseRequest.getStyle(),
                        exerciseRequest.getLength(),
                        exerciseRequest.getCalories(),
                        exerciseRequest.getTags(),
                        multipartFile);
            }
        }
        return stringMessageHandler.error(messageSource.getMessage("error.exercise.failedToAdd", null, LocaleContextHolder.getLocale()));
    }
    /**
     * Retrieves details of an exercise based on its ID.
     * If the exercise is found, the details are encapsulated in an ExerciseDTO object and returned as a success response.
     * Otherwise, an error response is returned.
     *
     * @param id The ID of the exercise to retrieve.
     * @return ResponseEntity containing the exercise details or an error message.
     */
    public ResponseEntity<?> getExercise(long id) {
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
            String jsonList;
            try {
                jsonList = objectMapper.writeValueAsString(tagList);
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println(jsonList);

            if (g.isPresent()) {
                GymWorkout gymWorkout = g.get();
                ExerciseDTO exerciseRequest = new ExerciseDTO(
                        exercise.getName(),
                        exercise.getName_eng(),
                        exercise.getDescription(),
                        exercise.getDescription_eng(),
                        "GYM",
                        gymWorkout.getRepetitions(),
                        gymWorkout.getSeries(),
                        exercise.getCaloriesBurned(),
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
                        "RUN",
                        run.getRuncategory().toString(),
                        run.getLenglhinmeters(),
                        exercise.getCaloriesBurned(),
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
                        "SWIMMING",
                        swimming.getSwimmingstyle().toString(),
                        swimming.getLenglhinmeters(),
                        exercise.getCaloriesBurned(),
                        jsonList);
                return exerciseMessageHandler.success(exerciseRequest);
            }

        }
        return stringMessageHandler.error(messageSource.getMessage("error.exercise.invalidID", null, LocaleContextHolder.getLocale()));
    }
    /**
     * Retrieves the picture associated with an exercise based on its ID.
     * If the exercise is found, its picture data is converted to a Base64-encoded string and returned as a success response.
     * Otherwise, an error response is returned.
     *
     * @param id The ID of the exercise to retrieve the picture for.
     * @return ResponseEntity containing the Base64-encoded picture data or an error message.
     */
    public ResponseEntity<?> getExercisePicture(long id) {
        Optional<Exercise> e = exerciseRepository.findExerciseById(id);
        if (e.isPresent()) {
            Exercise exercise = e.get();
            byte[] picture = exercise.getPicture().getPictureData();
            String pictureBytes = Base64.getEncoder().encodeToString(picture);
            return stringMessageHandler.success(pictureBytes);
        }
        return stringMessageHandler.error(messageSource.getMessage("error.invalidID", null, LocaleContextHolder.getLocale()));
    }
    /**
     * Adds a new running exercise with the provided details.
     * The exercise is created along with its associated Run entity.
     * Returns a success response containing the ID of the newly created exercise.
     *
     * @param name           The name of the exercise.
     * @param description    The description of the exercise.
     * @param name_eng       The English name of the exercise.
     * @param description_eng The English description of the exercise.
     * @param category       The category of the running exercise.
     * @param lengthInMeters The length of the running exercise in meters.
     * @param calories       The number of calories that are burned while performing exercise
     * @param tags           The tags associated with the exercise.
     * @param multipartFile  The multipart file containing the exercise picture.
     * @return ResponseEntity containing the ID of the newly created exercise or an error message.
     */
    public ResponseEntity<?> addNewRunExercise(String name, String description, String name_eng, String description_eng, String category, int lengthInMeters, int calories, List<String> tags, MultipartFile multipartFile) {
        Exercise exercise = createExercise(multipartFile, name, description, name_eng, description_eng,calories, tags);
        Run run = new Run(lengthInMeters, RunCategory.valueOf(category), exercise);
        runRepository.save(run);
        return longMessageHandler.success(exercise.getId());
    }
    /**
     * Adds a new gym exercise with the provided details.
     * The exercise is created along with its associated GymWorkout entity.
     * Returns a success response containing the ID of the newly created exercise.
     *
     * @param name           The name of the exercise.
     * @param description    The description of the exercise.
     * @param name_eng       The English name of the exercise.
     * @param description_eng The English description of the exercise.
     * @param repetitions    The number of repetitions for the gym exercise.
     * @param series         The number of series for the gym exercise.
     * @param calories       The number of calories that are burned while performing exercise
     * @param tags           The tags associated with the exercise.
     * @param multipartFile  The multipart file containing the exercise picture.
     * @return ResponseEntity containing the ID of the newly created exercise or an error message.
     */
    public ResponseEntity<?> addNewGymExercise(String name, String description, String name_eng, String description_eng, int repetitions, int series,int calories, List<String> tags, MultipartFile multipartFile) {
        Exercise exercise = createExercise(multipartFile, name, description, name_eng, description_eng,calories, tags);
        GymWorkout gymWorkout = new GymWorkout(series, repetitions, exercise);
        gymWorkoutRepository.save(gymWorkout);
        return longMessageHandler.success(exercise.getId());
    }
    /**
     * Adds a new swimming exercise with the provided details.
     * The exercise is created along with its associated Swimming entity.
     * Returns a success response containing the ID of the newly created exercise.
     *
     * @param name           The name of the exercise.
     * @param description    The description of the exercise.
     * @param name_eng       The English name of the exercise.
     * @param description_eng The English description of the exercise.
     * @param style          The swimming style for the exercise.
     * @param lengthInMeters The length of the swimming exercise in meters.
     * @param calories       The number of calories that are burned while performing exercise
     * @param tags           The tags associated with the exercise.
     * @param multipartFile  The multipart file containing the exercise picture.
     * @return ResponseEntity containing the ID of the newly created exercise or an error message.
     */
    public ResponseEntity<?> addNewSwimmingExercise(String name, String description, String name_eng, String description_eng, String style, int lengthInMeters, int calories, List<String> tags, MultipartFile multipartFile) {
        Exercise exercise = createExercise(multipartFile, name, description, name_eng, description_eng,calories, tags);
        Swimming swimming = new Swimming(lengthInMeters, SwimmingStyle.valueOf(style), exercise);
        swimmingRepository.save(swimming);
        return longMessageHandler.success(exercise.getId());
    }
    /**
     * Handles the processing of tags associated with an exercise.
     * Tags are converted to uppercase and checked against predefined exercise tags.
     * If a tag matches, it is saved and associated with the exercise.
     *
     * @param tags     The list of tags associated with the exercise.
     * @param exercise The exercise to associate the tags with.
     */
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
                exercise.addTagExercise(tag2);
                tagRepository.save(tag2);
            }
        }
    }
    /**
     * Converts a multipart file to its byte array representation.
     *
     * @param f The multipart file to convert.
     * @return The byte array representing the contents of the multipart file.
     */
    public byte[] handlePicture(MultipartFile f) {
        try {
            return f.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Creates a new exercise entity based on the provided details.
     * The exercise is associated with a picture and tags.
     *
     * @param multipartFile  The multipart file containing the exercise picture.
     * @param name           The name of the exercise.
     * @param description    The description of the exercise.
     * @param name_eng       The English name of the exercise.
     * @param description_eng The English description of the exercise.
     * @param tags           The tags associated with the exercise.
     * @return The newly created Exercise entity.
     */
    public Exercise createExercise(MultipartFile multipartFile, String name, String description, String name_eng, String description_eng,int calories, List<String> tags) {
        byte[] pictureData = handlePicture(multipartFile);
        Picture picture = new Picture(pictureData);
        pictureRepository.save(picture);
        Exercise exercise = new Exercise(name, name_eng, picture, description, description_eng,calories);
        exerciseRepository.save(exercise);
        handleTags(tags, exercise);
        return exercise;
    }
}
