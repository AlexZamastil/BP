package cz.uhk.fim.project.bakalarka.util;

import org.springframework.http.ResponseEntity;

public class MessageHandler {
    public static ResponseEntity<Object> error(String errorText){
        return ResponseEntity.badRequest().body(errorText);
    }
    public static ResponseEntity<Object> success(String text){
        return ResponseEntity.ok(text);
    }


}
