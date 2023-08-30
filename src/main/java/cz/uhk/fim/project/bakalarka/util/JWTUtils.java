package cz.uhk.fim.project.bakalarka.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.Verification;
import org.springframework.stereotype.Component;

@Component
public class JWTUtils {

    private final String secret = "SECRET";


    public String generateJWToken(Long userID) {
        JWTCreator.Builder jwtBuilder = JWT
                .create()
                .withClaim("PK",userID);

        Algorithm algorithm = Algorithm.HMAC256(secret);

        return "{\"jwt\": \"" + jwtBuilder.sign(algorithm) + "\"}";
    }

    public Claim getID(String token) {
        return JWT
                .require(Algorithm.HMAC256(secret))
                .build()
                .verify(token)
                .getClaim("PK");
    }


}
