package cz.uhk.fim.project.bakalarka.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final List<String> allowedMethods = Arrays.asList("GET","POST");
    private final List<String> allowedHeaders = Arrays.asList("Content-Type","X-XSRF-TOKEN", "Authorization");
    AuthFilter authFilter;

@Autowired
    public SecurityConfig(AuthFilter authFilter) {
        this.authFilter = authFilter;
    }


    @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception  {
        http
                //.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                //.and()
                .authorizeRequests()
                .anyRequest().permitAll()
                .and()
                .httpBasic(Customizer.withDefaults())
                .csrf().disable();

        http.cors();

        http.addFilterBefore(authFilter, BasicAuthenticationFilter.class);

        return http.build();
   }

   @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("*"));
        config.setAllowedMethods(allowedMethods);
        config.setAllowedHeaders(allowedHeaders);

       UrlBasedCorsConfigurationSource urlCorfConfig = new UrlBasedCorsConfigurationSource();
       urlCorfConfig.registerCorsConfiguration("/api/**", config);

       return urlCorfConfig;
   }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
 /*
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest()
                        .permitAll())
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
    */

}
