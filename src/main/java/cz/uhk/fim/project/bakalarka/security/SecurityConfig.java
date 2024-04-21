package cz.uhk.fim.project.bakalarka.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Configuration class for defining security configurations.
 * Configures authentication, authorization, CORS, CSRF protection, and HTTP security headers.
 *
 * @author Alex Zamastil
 */
@Configuration
@Log4j2
@EnableWebSecurity
public class SecurityConfig {

    private final List<String> allowedMethods = Arrays.asList("GET", "POST", "OPTIONS", "DELETE", "PUT");
    private final List<String> allowedHeaders = Arrays.asList("Content-Type", "X-XSRF-TOKEN", "Authorization", "XSRF-TOKEN", "Accept", "Accept-Language");
    @Value("${security.cors.origins}")
    private final List<String> allowedOrigins = Collections.emptyList();
    AuthFilter authFilter;

    @Autowired
    public SecurityConfig(AuthFilter authFilter) {
        this.authFilter = authFilter;
    }


    /**
     * Configures the security filter chain for HTTP requests.
     * This method sets up headers, CORS, CSRF protection, and request authorization.
     * It specifies which HTTP requests require authentication and authorization using role-based access control.
     *
     * @param http HttpSecurity object for configuring security settings.
     * @return SecurityFilterChain instance representing the configured security filter chain.
     * @throws Exception if an error occurs while configuring the security filter chain.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .headers(headers ->
                        headers.xssProtection(xXssConfig -> xXssConfig.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
                                .contentSecurityPolicy(contentSecurityPolicyConfig -> contentSecurityPolicyConfig.policyDirectives("script-src 'self'")))

                .cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf().csrfTokenRepository(csrfTokenRepository())
                .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                .and()
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(HttpMethod.POST, "/api/user/passwordReset").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/user/updateData").hasAnyRole("USER","ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/user/getUserData").hasAnyRole("USER","ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/generateTraining").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/getTrainings").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/hasActiveTraining/{id}").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/training/getActiveTraining/{id}").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/trainJ48").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/getExerciseTags").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/getFoodTags").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/getTimingTags").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/getExercise/{id}").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/exercise/addExercise").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/user/login").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/user/logout").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/user/register").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/initConnection").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/api/user/deleteUser").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/user/addAverageValues").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/training/deleteTraining/{id}").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/training/getTrainings").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/food/addFood").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/food/getFood/{id}").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/food/getFood/picture/{id}").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.POST, "api/initConnection").permitAll()


                                .anyRequest().permitAll())
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(authFilter, BasicAuthenticationFilter.class);





        return http.build();
    }

    /**
     * Provides a CorsConfigurationSource bean for configuring Cross-Origin Resource Sharing (CORS) settings.
     * This method sets up CORS configuration with allowed origins, methods, headers, and credentials.
     *
     * @return CorsConfigurationSource instance with configured CORS settings.
     */

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(allowedOrigins);
        config.setAllowedMethods(allowedMethods);
        config.setAllowedHeaders(allowedHeaders);
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource urlCORFConfig = new UrlBasedCorsConfigurationSource();
        urlCORFConfig.registerCorsConfiguration("/api/**", config);

        return urlCORFConfig;
    }


    /**
     * Provides a CookieCsrfTokenRepository bean for managing Cross-Site Request Forgery (CSRF) tokens.
     * This method configures the repository with the cookie name, security flag and cookie path.
     *
     * @return CookieCsrfTokenRepository instance with configured settings.
     */

    @Bean
    public CookieCsrfTokenRepository csrfTokenRepository() {
        CookieCsrfTokenRepository repository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        repository.setCookieName("XSRF-TOKEN");
        repository.setSecure(true);
        repository.setCookiePath("/");

        return repository;
    }


    /**
     * Provides a PasswordEncoder bean for encoding passwords securely.
     * Password hashing ensures that passwords are stored securely in the database - not in plaintext,
     * protecting user credentials from unauthorized access even in the event of a data breach.
     *
     * @return BCryptPasswordEncoder instance for password encoding.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
