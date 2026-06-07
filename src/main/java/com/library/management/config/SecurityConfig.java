package com.library.management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, "/api/books/**").hasAnyRole("LIBRARIAN", "STUDENT")
                .requestMatchers(HttpMethod.POST, "/api/books/**").hasRole("LIBRARIAN")
                .requestMatchers(HttpMethod.PUT, "/api/books/**").hasRole("LIBRARIAN")
                .requestMatchers(HttpMethod.DELETE, "/api/books/**").hasRole("LIBRARIAN")
                
                .requestMatchers(HttpMethod.GET, "/api/students/{id}/books").hasAnyRole("LIBRARIAN", "STUDENT")
                .requestMatchers(HttpMethod.GET, "/api/students/{id}").hasAnyRole("LIBRARIAN", "STUDENT")
                .requestMatchers(HttpMethod.GET, "/api/students").hasRole("LIBRARIAN")
                .requestMatchers(HttpMethod.POST, "/api/students/**").hasRole("LIBRARIAN")
                .requestMatchers(HttpMethod.PUT, "/api/students/**").hasRole("LIBRARIAN")
                .requestMatchers(HttpMethod.DELETE, "/api/students/**").hasRole("LIBRARIAN")
                
                .requestMatchers("/api/loans/**").hasRole("LIBRARIAN")
                
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
