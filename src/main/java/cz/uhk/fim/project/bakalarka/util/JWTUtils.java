package cz.uhk.fim.project.bakalarka.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
@Log4j2
public class JWTUtils {

    private final String secret = "SECRET";
    HashHandler hashHandler = new HashHandler();

    @Value("${spring.security.secret-var}")
    String secretVar;

    public JWTUtils() {
    }
    public String generateJWToken(Long userID) {
        String var = hashHandler.hashString(secretVar);
        long day = 7;
        Date expirationDate = Date.from(new Date().toInstant().plus(day, ChronoUnit.DAYS));

        JWTCreator.Builder jwtBuilder = JWT
                .create()
                .withClaim("PK", userID)
                .withClaim("org", "UHK-FIM")
                .withClaim("var", var)
                .withExpiresAt(expirationDate);

        Algorithm algorithm = Algorithm.HMAC256(secret);
        //return "{\"jwt\": \"" + jwtBuilder.sign(algorithm) + "\"}";
        return jwtBuilder.sign(algorithm);
    }


    public Claim getID(String token) {
        return JWT
                .require(Algorithm.HMAC256(secret))
                .build()
                .verify(token)
                .getClaim("PK");
    }

    public Claim getOrg(String token) {
        return JWT
                .require(Algorithm.HMAC256(secret))
                .build()
                .verify(token)
                .getClaim("org");
    }

    public boolean isTokenExpired(String token) {
        try {
            DecodedJWT jwt = JWT.require(Algorithm.HMAC256(secret))
                    .build()
                    .verify(token);

            Date expirationDate = jwt.getExpiresAt();
            return expirationDate != null && expirationDate.before(new Date());
        } catch (TokenExpiredException e) {

            return true;
        } catch (Exception e) {

            return false;
        }
    }

    public boolean isTokenLegitimate(String token) {

        DecodedJWT decodedJWT = JWT.decode(token);

        java.util.Map<String, Claim> claims = decodedJWT.getClaims();

        int claimSize = claims.size();

        int numberOfClaims = 4;

        if (!((claimSize == numberOfClaims) && (getOrg(token).asString().equals("UHK-FIM") && decodedJWT.getAlgorithm().equals("HS256")))) {
            log.info("Token is invalid");
            log.info("Claims: " + claimSize);
            log.info(claims);
            log.info("ORG: " + getOrg(token).asString());
            log.info("algorhitm: " + decodedJWT.getAlgorithm());
            return false;
        }
        if (!(hashHandler.verifyHash(JWT
                .require(Algorithm.HMAC256(secret))
                .build()
                .verify(token)
                .getClaim("var")
                .asString(), secretVar))) {

            log.info("Hash handler output: " + hashHandler.verifyHash(JWT
                    .require(Algorithm.HMAC256(secret))
                    .build()
                    .verify(token)
                    .getClaim("var")
                    .asString(), secretVar));

            log.info(JWT
                    .require(Algorithm.HMAC256(secret))
                    .build()
                    .verify(token)
                    .getClaim("var")
                    .asString());
            log.info(secretVar);

            log.info("hash not matching");
            return false;
        }
        return true;
    }
}
