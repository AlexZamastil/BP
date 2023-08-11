package cz.uhk.fim.project.bakalarka.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

@Component
public class JWTUtils {

    private final String secret = "SECRET";


    public String generateJWToken(String userEmail) {
        JWTCreator.Builder jwtBuilder = JWT
                .create()
                .withSubject(userEmail);

        Algorithm algorithm = Algorithm.HMAC256(secret);

        return jwtBuilder.sign(algorithm);
    }

    public String getEmail(String token) {
        return JWT
                .require(Algorithm.HMAC512(secret))
                .build()
                .verify(token)
                .getSubject();
    }




}
