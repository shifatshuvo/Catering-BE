package com.example.catering_service_practice.controller;


import com.example.catering_service_practice.controller.dto.UserDto;
import com.example.catering_service_practice.exception.BadRequestException;
import com.example.catering_service_practice.model.User;
import com.example.catering_service_practice.model.auth.AuthToken;
import com.example.catering_service_practice.repository.TokenRepository;
import com.example.catering_service_practice.repository.UserRepository;
import com.example.catering_service_practice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenRepository tokenRepository;


    // update an user
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@RequestHeader(AUTHORIZATION) String token, @RequestBody User user, @PathVariable Long id) {
        User authUser = authenticate(token);
        if (userService.updateUser(authUser, user, id)) {
            return ResponseEntity.ok("User updated successfully");
        } else {
            return ResponseEntity.badRequest().body("User not found or invalid data");
        }
    }


    // get all user
    @GetMapping
    public List<UserDto> getAllUser() {
        return userService.findAllUsers();
    }


    // get an user
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getOneUser(@PathVariable Long id) {
        Optional<UserDto> user = userService.findOneUser(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteUserById(@RequestHeader(AUTHORIZATION) String token, @PathVariable Long id) {
//        User authUser = authenticate(token);
//        if (userService.deleteUserById(authUser, id)) {
//            return ResponseEntity.ok("User updated successfully");
//        } else {
//            return ResponseEntity.badRequest().body("User not found or invalid data");
//        }
//    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
//        try {
//            userService.deleteUserById(id);
//            return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete User");}
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        try {
            userService.deleteUserById(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete User");
        }
    }


    // check user token
    private User authenticate(String token) {
        Optional<AuthToken> optionalToken = tokenRepository.findByTokenStr(token);
        if (optionalToken.isEmpty()) {
            throw new BadRequestException("Invalid Token");
        }
        return optionalToken.get().getUser();
    }

}
















//    @PostMapping
//    private ResponseEntity<String> save(@RequestBody User user) {
//        String response;
//        try {
//            response = userService.storeUser(user);
//            if (response.equals("Email already in use")) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//            } else {
//                return ResponseEntity.ok(response);
//            }
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }