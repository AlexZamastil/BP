package cz.uhk.fim.project.bakalarka.security;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
public class SecurityConfig {
    AuthFilter authFilter;


    public SecurityConfig(AuthFilter authFilter) {
        this.authFilter = authFilter;
    }

   // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws IOException {}


}
