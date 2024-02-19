package cz.uhk.fim.project.bakalarka.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtils {

    private final String secret = "SECRET";
    HashHandler hashHandler = new HashHandler();

    @Value("${spring.security.secret-var}")
    String secretVar;


    public String generateJWToken(Long userID) {
        String var = hashHandler.hashString(secretVar);
        long expirationTimeMillis = 14400000;
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTimeMillis);

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

    public boolean isTokenLegitimate(String token){

        DecodedJWT decodedJWT = JWT.decode(token);

        java.util.Map<String, Claim> claims = decodedJWT.getClaims();

        int claimSize = claims.size();

        int numberOfClaims = 3;
        return hashHandler.verifyHash(JWT
                .require(Algorithm.HMAC256(secret))
                .build()
                .verify(token)
                .getClaim("var")
                .asString(),secretVar) && (claimSize == numberOfClaims) && (getOrg(token).asString().equals("UHK-FIM") && decodedJWT.getAlgorithm().equals("HS256"));
    }


}
