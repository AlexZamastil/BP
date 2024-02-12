package cz.uhk.fim.project.bakalarka.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class HashHandler {
    public HashHandler() {
    }

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    public String hashString(String string) {
        return bCryptPasswordEncoder.encode(string);
    }

    public boolean verifyHash(String hashedString,String original) {
        return bCryptPasswordEncoder.matches(hashedString,original);
    }

}
