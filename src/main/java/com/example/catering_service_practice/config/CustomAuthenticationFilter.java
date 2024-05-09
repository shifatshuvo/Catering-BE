package com.example.catering_service_practice.config;

import com.example.catering_service_practice.model.User;
import com.example.catering_service_practice.model.auth.AuthToken;
import com.example.catering_service_practice.repository.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.AuthenticationManager;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class CustomAuthenticationFilter extends OncePerRequestFilter {
    private AuthenticationManager authManager;
    private TokenRepository tokenRepository;

    public CustomAuthenticationFilter(AuthenticationManager authManager, TokenRepository tokenRepository) {
        this.authManager = authManager;
        this.tokenRepository = tokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(token)) {
            Optional<AuthToken> optionalToken = tokenRepository.findByTokenStr(token);
            if (optionalToken.isPresent()) {
                User user = optionalToken.get().getUser();

                // inject user details to spring security context.
                UsernamePasswordAuthenticationToken userDetails = new UsernamePasswordAuthenticationToken(user.getId(), user.getPassword(), List.of(new SimpleGrantedAuthority(user.getRole().toUpperCase())));
                SecurityContextHolder.getContext().setAuthentication(userDetails);
            }
        }
        filterChain.doFilter(request, response);
    }
}
