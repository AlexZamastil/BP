package cz.uhk.fim.project.bakalarka.security;

import cz.uhk.fim.project.bakalarka.DataAccessObjects.UserRepository;
import cz.uhk.fim.project.bakalarka.model.User;
import cz.uhk.fim.project.bakalarka.util.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
            boolean isPrivileged = request.getServletPath().startsWith("/api/privileged/");
            boolean isAuthorized = request.getServletPath().startsWith("/api/authorized/");
            boolean isNonauthorized = request.getServletPath().startsWith("/api/nonauthorized/");

                    if(isNonauthorized){
                        filterChain.doFilter(request,response);
                    }
                        else {
                        if (isAuthorized || isPrivileged) {
                            try {
                                String token = request.getHeader("Authorization").replace("Bearer ","");
                                System.out.println(token);
                                if(token.equals("")){
                                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                }

                                String userEmail = jwtUtils.getEmail(token);
                                User user = userRepository.findUserByEmail(userEmail);

                                if(Objects.equals(user.getToken(), token)){
                                    if (isPrivileged) {
                                        if (user.isAdminPrivileges()) {
                                            filterChain.doFilter(request, response);
                                        } else response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                    }
                                }

                                filterChain.doFilter(request, response);
                            } catch (IOException | ServletException e) {
                                e.printStackTrace();
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            }


                        }
                        else  response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    }


    }

}
