package cz.uhk.fim.project.bakalarka.util;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class MessageHandler<T> {
    public ResponseEntity<T> error(T t){
        return ResponseEntity.badRequest().body(t);
    }
    public ResponseEntity<T> success(T t){
        return ResponseEntity.ok(t);
    }


}
