package cz.uhk.fim.project.bakalarka.util;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class MessageHandler {
    public static ResponseEntity<Object> error(Object o){
        return ResponseEntity.badRequest().body(o);
    }
    public static ResponseEntity<Object> success(Object o){
        return ResponseEntity.ok(o);
    }


}
