package com.example.catering_service_practice.controller;


import com.example.catering_service_practice.controller.dto.SignInDto;
import com.example.catering_service_practice.controller.dto.SignInResponseDto;
import com.example.catering_service_practice.model.User;
import com.example.catering_service_practice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;


    // sign up a user
    @PostMapping("/sign-up")
    private ResponseEntity<String> saveUser(@RequestBody User user) {
        String response = authService.storedUser(user);
        if (response.equals("Email or password cannot be empty!") || response.equals("Email already in use!")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else {
            return ResponseEntity.ok(response);
        }
    }


    // sign in a user
    @PostMapping("/sign-in")
    public SignInResponseDto doSignIn(@RequestBody @Valid SignInDto dto) {
        return authService.signIn(dto);
    }
}
