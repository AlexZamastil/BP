package cz.uhk.fim.project.bakalarka.util;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class HashHandler {
    public HashHandler() {
    }

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    public String hashString(String string) {
        return bCryptPasswordEncoder.encode(string);
    }

    public boolean verifyHash(String hashedString,String original) {
        return bCryptPasswordEncoder.matches(original,hashedString);
    }

}
