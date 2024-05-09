package com.example.catering_service_practice.service;


import com.example.catering_service_practice.controller.dto.SignInDto;
import com.example.catering_service_practice.controller.dto.SignInResponseDto;
import com.example.catering_service_practice.exception.UserNotFoundException;
import com.example.catering_service_practice.model.User;
import com.example.catering_service_practice.model.auth.AuthToken;
import com.example.catering_service_practice.repository.TokenRepository;
import com.example.catering_service_practice.repository.UserRepository;
import com.example.catering_service_practice.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.util.StringUtils.hasText;

@Service
public class AuthService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;


    // save a user
    public String storedUser(User user) {
        if (!hasText(user.getEmail()) || !hasText(user.getPassword())) {
            return "Email or password cannot be empty!";
        }

        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            return ("Email already in use!");
        }

        // Convert role to uppercase
        user.setRole(user.getRole().toUpperCase());

        userRepository.save(user);
        return "User registered successfully";
    }


    // login a user
    public SignInResponseDto signIn(SignInDto dto) {

        Optional<User> optionalUser = userRepository.findByEmailIgnoreCaseAndPassword(dto.getEmail(), dto.getPassword());
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User not found!!");
        }
        User user = optionalUser.get();
        AuthToken token = new AuthToken();
        token.setUser(user);
        token.setTokenStr(Utils.randomString(16));
        tokenRepository.save(token);

        SignInResponseDto response = new SignInResponseDto();
        response.setUser(user);
        response.setTokenStr(token.getTokenStr());
        return response;
    }
}
