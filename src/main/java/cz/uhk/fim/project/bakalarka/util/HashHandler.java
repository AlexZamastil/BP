package cz.uhk.fim.project.bakalarka.util;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Utility class for generating and validating hashed strings.
 * Uses the BCrypt hashing algorithm provided by Spring Security for hashing and verifying strings.
 *
 * @author Alex Zamastil
 */
@Component
@Log4j2
public class HashHandler {
    public HashHandler() {
    }

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    /**
     * Hashes the given string using the BCryptPasswordEncoder.
     *
     * @param string The string to hash.
     * @return The hashed representation of the input string.
     */
    public String hashString(String string) {
        return bCryptPasswordEncoder.encode(string);
    }


    /**
     * Verifies whether the original string matches the hashed string.
     *
     * @param hashedString The hashed string to compare.
     * @param original     The original string to compare against the hashed string.
     * @return True if the original string matches the hashed string, otherwise false.
     */
    public boolean verifyHash(String hashedString, String original) {
        return bCryptPasswordEncoder.matches(original, hashedString);
    }

}
