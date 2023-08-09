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
            boolean isRequestPrivileged = request.getServletPath().startsWith("api/privileged/");

                    if(!isRequestPrivileged || request.getServletPath().startsWith("api/basic/")){
                        filterChain.doFilter(request,response);
                    }
                        else {
                            String token =  request.getHeader("Authorization");
                            String userEmail = jwtUtils.getEmail(token);
                            User user = userRepository.findUserByEmail(userEmail);
                            if (user.isAdminPrivileges()){
                                filterChain.doFilter(request,response);
                            }
                            else {
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            }
                    }


    }

}
