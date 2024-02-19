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

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        log.info(request.getRequestURI());

        if (!StringUtils.hasText(request.getHeader("Authorization"))) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            log.info(request.getHeader("Authorization"));
            System.out.println(request.getHeader("Authorization"));
            String token = request.getHeader("Authorization").replace("Bearer ", "");
            if (token.equals("")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
            if (jwtUtils.isTokenExpired(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
            if (!jwtUtils.isTokenLegitimate(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
            Claim userIDClaim = jwtUtils.getID(token);
            log.info(userIDClaim + " = User ID from jwtToken");
            User user = userRepository.findUserById(userIDClaim.asLong());
            if (user == null) return;
            if (Objects.equals(user.getToken(), token)) {
                filterChain.doFilter(request, response);
            }
        } catch (IOException | ServletException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    }

}

