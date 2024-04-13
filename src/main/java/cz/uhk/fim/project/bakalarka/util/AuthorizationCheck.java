package cz.uhk.fim.project.bakalarka.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

/**
 * Utility class for checking the presence of authorization headers in HTTP requests.
 *
 * @author Alex Zamastil
 */
public class AuthorizationCheck {
    /**
     * Checks whether the provided HttpServletRequest contains an "Authorization" header.
     *
     * @param request The HttpServletRequest object to check.
     * @return True if the "Authorization" header is present and not empty, otherwise false.
     */
    public static boolean hasAuthorization(HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        return (StringUtils.hasText(authorizationHeader));
    }
}
