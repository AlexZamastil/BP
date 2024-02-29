package cz.uhk.fim.project.bakalarka.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

public class AuthorizationCheck {
    public static boolean hasAuthorization(HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        return (StringUtils.hasText(authorizationHeader));
    }
}
