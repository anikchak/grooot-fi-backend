package com.grooot.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    private final JwtAuthenticationEntryPoint unauthorizedHandler;

    // You can inject your custom JWT filter and entry point (to handle errors) via constructor injection
    public SecurityConfig(JwtAuthenticationFilter jwtFilter,
                          JwtAuthenticationEntryPoint unauthorizedHandler) {
        this.jwtFilter = jwtFilter;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // disable CSRF (since you’re using stateless JWT authentication)
            .csrf(csrf -> csrf.disable())
            // set custom unauthorized handler so that JWT errors are handled gracefully
            .exceptionHandling(exception ->
                exception.authenticationEntryPoint(unauthorizedHandler))
            // make the session stateless (we are using JWT so no HTTP session is needed)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // configure URL-based authorization rules
            .authorizeHttpRequests(auth -> auth
                // allow unauthenticated requests to your social login endpoints
                .requestMatchers("/auth/**", "/oauth2/**").permitAll()
                // all other endpoints require a valid JWT
                .anyRequest().authenticated()
            );

        // add your custom JWT filter before Spring Security’s UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
    // Optionally, if you need to expose the AuthenticationManager bean:
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
