package cz.uhk.fim.project.bakalarka.service;

import cz.uhk.fim.project.bakalarka.enumerations.Tag_Exercise;
import cz.uhk.fim.project.bakalarka.enumerations.Tag_Food;
import cz.uhk.fim.project.bakalarka.enumerations.Tag_Timing;
import cz.uhk.fim.project.bakalarka.util.MessageHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class TagService {
    MessageHandler<String> messageHandler = new MessageHandler<>();
    public TagService() {
    }
    public ResponseEntity<?> getExerciseTags(){

        return messageHandler.success(Arrays.toString(Tag_Exercise.values()));
    }
    public ResponseEntity<?> getFoodTags(){
        return messageHandler.success(Arrays.toString(Tag_Food.values()));
    }
    public ResponseEntity<?> getTimingTags(){
        return messageHandler.success(Arrays.toString(Tag_Timing.values()));
    }

}
