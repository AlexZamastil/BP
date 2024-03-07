package cz.uhk.fim.project.bakalarka.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final List<String> allowedMethods = Arrays.asList("GET", "POST","OPTIONS","DELETE","PUT");
    private final List<String> allowedHeaders = Arrays.asList("Content-Type", "X-XSRF-TOKEN", "Authorization","XSRF-TOKEN", "Accept","Localization");
    @Value("${security.cors.origins}")
    private final List<String> allowedOrigins = Collections.emptyList();

    AuthFilter authFilter;

    @Autowired
    public SecurityConfig(AuthFilter authFilter) {
        this.authFilter = authFilter;
    }

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
                                .requestMatchers(HttpMethod.POST,"/user/passwordReset").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.POST,"/user/updateData").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.GET,"/user/getUserData").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.POST,"/generateTraining").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.GET,"/hasActiveTraining/{id}").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.POST,"/utrainJ48").permitAll()
                                .requestMatchers(HttpMethod.GET,"/getExerciseTags").permitAll()
                                .requestMatchers(HttpMethod.GET,"/getFoodTags").permitAll()
                                .requestMatchers(HttpMethod.GET,"/getTimingTags").permitAll()
                                .requestMatchers(HttpMethod.GET,"/getExercise/{id}").permitAll()
                                .requestMatchers(HttpMethod.POST,"/addExercise").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST,"/user/login").permitAll()
                                .requestMatchers(HttpMethod.POST,"/user/register").permitAll()
                                .requestMatchers(HttpMethod.POST,"/language/switch").permitAll()
                                .anyRequest().permitAll())
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(authFilter, BasicAuthenticationFilter.class);

        return http.build();
    }

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

    @Bean
    public CookieCsrfTokenRepository csrfTokenRepository() {
        CookieCsrfTokenRepository repository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        repository.setCookieName("XSRF-TOKEN");
        repository.setSecure(true);
        repository.setCookiePath("/");

        return repository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
