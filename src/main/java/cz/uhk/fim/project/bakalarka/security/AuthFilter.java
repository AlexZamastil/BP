package cz.uhk.fim.project.bakalarka.security;

import com.auth0.jwt.interfaces.Claim;
import cz.uhk.fim.project.bakalarka.DAO.UserRepository;
import cz.uhk.fim.project.bakalarka.model.User;
import cz.uhk.fim.project.bakalarka.util.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

/**
 * Authentication filter responsible for validating JSON Web Tokens (JWT) in incoming HTTP requests.
 * It intercepts requests, extracts the JWT token from the Authorization header
 * and performs various checks to ensure the token's validity.
 * If the token is valid, it verifies the user's identity and grants access to protected resources.
 * Otherwise, it returns HttpServletResponse.SC_UNAUTHORIZED.
 *
 * @author Alex Zamastil
 */
@Log4j2
@Component
public class AuthFilter extends OncePerRequestFilter {
    JWTUtils jwtUtils;
    UserRepository userRepository;

    @Autowired
    public AuthFilter(JWTUtils jwtUtils, UserRepository userRepository) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    /**
     * Filters incoming HTTP requests and validates JWT tokens.
     *
     * @param request     incoming HttpServletRequest request.
     * @param response    outgoing HttpServletResponse response.
     * @param filterChain FilterChain object for invoking the next filter in the chain.
     * @throws ServletException if an error occurs during the filter execution throws ServletException.
     * @throws IOException      if an I/O error occurs while processing the request throws I/O exception.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        if (!StringUtils.hasText(request.getHeader("Authorization"))) {
            filterChain.doFilter(request, response);
            return;
        }

        log.info("JWT TOKEN " + request.getHeader("Authorization"));
        String token = request.getHeader("Authorization");
        if (!StringUtils.hasText(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token empty");
            log.info("Token empty");
            return;
        }
        if (jwtUtils.isTokenExpired(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token expired");
            log.info("Token expired");
            return;
        }
        if (!jwtUtils.isTokenLegitimate(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token is not valid");
            log.info("Token is not valid: " + token);
            return;
        }
        try {
            Claim userIDClaim = jwtUtils.getID(token);
            log.info(userIDClaim + " = User ID from jwtToken");
            User user = userRepository.findUserById(userIDClaim.asLong());
            if (user == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("User from token not found");
                log.info("User from token not found : " + token);
                return;
            }
            if (Objects.equals(user.getToken(), token)) {
                filterChain.doFilter(request, response);
            } else {
                log.error("Token does not match current value in database");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } catch (IOException | ServletException e) {
            log.error("An error occurred while trying to get data from the token.", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    }

}

