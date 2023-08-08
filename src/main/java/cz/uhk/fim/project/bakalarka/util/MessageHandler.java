package cz.uhk.fim.project.bakalarka.util;

import org.springframework.http.ResponseEntity;

public class MessageHandler {
    public ResponseEntity<Object> error(String errorText){
        return ResponseEntity.badRequest().body(errorText);
    }
    public ResponseEntity<Object> success(String text){
        return ResponseEntity.ok(text);
    }

}
