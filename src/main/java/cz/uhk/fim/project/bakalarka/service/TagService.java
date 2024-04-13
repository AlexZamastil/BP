package cz.uhk.fim.project.bakalarka.service;

import cz.uhk.fim.project.bakalarka.enumerations.Tag_Exercise;
import cz.uhk.fim.project.bakalarka.enumerations.Tag_Food;
import cz.uhk.fim.project.bakalarka.enumerations.Tag_Timing;
import cz.uhk.fim.project.bakalarka.util.MessageHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Service class responsible for providing tag-related functionalities.
 * Tags are enumerated values representing various categories or attributes used to classify exercises and food.
 *
 * @author Alex Zamastil
 */
@Service
public class TagService {
    MessageHandler<String> messageHandler = new MessageHandler<>();
    public TagService() {
    }
    /**
     * Retrieves exercise tags and returns a success response containing the tag values.
     *
     * @return ResponseEntity containing the exercise tags as a success response.
     */
    public ResponseEntity<?> getExerciseTags(){

        return messageHandler.success(Arrays.toString(Tag_Exercise.values()));
    }
    /**
     * Retrieves food tags and returns a success response containing the tag values.
     *
     * @return ResponseEntity containing the food tags as a success response.
     */
    public ResponseEntity<?> getFoodTags(){
        return messageHandler.success(Arrays.toString(Tag_Food.values()));
    }
    /**
     * Retrieves timing tags and returns a success response containing the tag values.
     *
     * @return ResponseEntity containing the timing tags as a success response.
     */
    public ResponseEntity<?> getTimingTags(){
        return messageHandler.success(Arrays.toString(Tag_Timing.values()));
    }

}
