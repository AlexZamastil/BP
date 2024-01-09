package cz.uhk.fim.project.bakalarka.service;

import cz.uhk.fim.project.bakalarka.DAO.*;
import cz.uhk.fim.project.bakalarka.enumerations.RunCategory;
import cz.uhk.fim.project.bakalarka.enumerations.SwimmingStyle;
import cz.uhk.fim.project.bakalarka.enumerations.Tag_Exercise;
import cz.uhk.fim.project.bakalarka.model.*;
import cz.uhk.fim.project.bakalarka.util.MessageHandler;
import jakarta.annotation.Nonnull;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ExerciseService {
    ExerciseRepository exerciseRepository;
    GymWorkoutRepository gymWorkoutRepository;
    RunRepository runRepository;
    SwimmingRepository swimmingRepository;

    PictureRepository pictureRepository;
    TagRepository tagRepository;

    public ExerciseService(ExerciseRepository exerciseRepository, GymWorkoutRepository gymWorkoutRepository, RunRepository runRepository, SwimmingRepository swimmingRepository, PictureRepository pictureRepository, TagRepository tagRepository) {
        this.exerciseRepository = exerciseRepository;
        this.gymWorkoutRepository = gymWorkoutRepository;
        this.runRepository = runRepository;
        this.swimmingRepository = swimmingRepository;
        this.pictureRepository = pictureRepository;
        this.tagRepository = tagRepository;
    }



    public ResponseEntity<?> addNewExercise(String name, String description, String name_eng, String description_eng, @Nonnull String type, String category_style, int lengthInMeters, List<String> tags) {
        System.out.println(tags);
        Exercise exercise = new Exercise(name, name_eng, description, description_eng);
        exerciseRepository.save(exercise);
        handleTags(tags, exercise);

        if (type.equals("RUN")) {
            Run run = new Run(lengthInMeters, RunCategory.valueOf(category_style), exercise);
            runRepository.save(run);

        } else {
            Swimming swimming = new Swimming(lengthInMeters, SwimmingStyle.valueOf(category_style), exercise);
            swimmingRepository.save(swimming);
        }

        return MessageHandler.success("Exercise saved successfufly");
    }

    public ResponseEntity<?> addNewExercise(String name, String description, String name_eng, String description_eng,@Nonnull String type, String category_style, int lengthInMeters, List<String> tags, MultipartFile multipartFile) {
        byte[] pictureData = handlePicture(multipartFile);
        Picture picture = new Picture(pictureData);
        pictureRepository.save(picture);
        Exercise exercise = new Exercise(name, name_eng, picture, description, description_eng);
        exerciseRepository.save(exercise);
        handleTags(tags, exercise);
        if (type.equals("RUN")) {
            Run run = new Run(lengthInMeters, RunCategory.valueOf(category_style), exercise);
            runRepository.save(run);

        } else {
            Swimming swimming = new Swimming(lengthInMeters, SwimmingStyle.valueOf(category_style), exercise);
            swimmingRepository.save(swimming);
        }

        return MessageHandler.success("Exercise saved successfufly");
    }

    public ResponseEntity<?> addNewExercise(String name, String description, String name_eng, String description_eng,@Nonnull int repetitions,@Nonnull int series, List<String> tags) {
        System.out.println(tags);
        Exercise exercise = new Exercise(name, name_eng, description, description_eng);
        GymWorkout gymWorkout = new GymWorkout(series, repetitions, exercise);
        exerciseRepository.save(exercise);
        gymWorkoutRepository.save(gymWorkout);
        handleTags(tags, exercise);

        return MessageHandler.success("Exercise saved successfufly");

    }

    public ResponseEntity<?> addNewExercise(String name, String description, String name_eng, String description_eng, int repetitions, int series, List<String> tags, MultipartFile multipartFile) {
        byte[] pictureData = handlePicture(multipartFile);
        Picture picture = new Picture(pictureData);
        pictureRepository.save(picture);
        Exercise exercise = new Exercise(name, name_eng, picture, description, description_eng);
        GymWorkout gymWorkout = new GymWorkout(series, repetitions, exercise);
        exerciseRepository.save(exercise);
        gymWorkoutRepository.save(gymWorkout);
        handleTags(tags, exercise);

        return MessageHandler.success("Exercise saved successfufly");
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
    public byte[] handlePicture(MultipartFile f){
        try {
            return f.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
