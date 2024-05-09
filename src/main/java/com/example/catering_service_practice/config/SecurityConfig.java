package com.example.catering_service_practice.config;


import com.example.catering_service_practice.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AuthenticationConfiguration authConfig;
    @Autowired
    private TokenRepository tokenRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // path permissions
        http.authorizeHttpRequests((requests) -> requests
            .requestMatchers("/", "/health", "/auth/**").permitAll()
            .anyRequest().authenticated()   // default for any url
        );

        // disable regular auth mechanism
        http.formLogin(loginForm -> loginForm.disable());
        http.httpBasic(basicSecurity -> basicSecurity.disable());
        http.csrf(csrf -> csrf.disable());
        http.sessionManagement(session -> session.sessionCreationPolicy(STATELESS));

        // add our auth mechanism
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authConfig.getAuthenticationManager(), tokenRepository);
        http.addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
