package com.example.catering_service_practice.service;


import com.example.catering_service_practice.controller.dto.UserDto;
import com.example.catering_service_practice.exception.ForbiddenException;
import com.example.catering_service_practice.model.User;
import com.example.catering_service_practice.repository.OrderRepository;
import com.example.catering_service_practice.repository.TokenRepository;
import com.example.catering_service_practice.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.util.StringUtils.hasText;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TokenRepository tokenRepository;


    // update user
    public boolean updateUser(User authUser, User user, Long id) {
        if (user == null || !hasText(user.getName())) {
            return false; // Invalid data
        }

        if (!authUser.getRole().equals("ADMIN") && !authUser.getId().equals(user.getId())) {
            throw new ForbiddenException("Forbidden");
        }

        try {
            User savedUser = userRepository.getUserById(user.getId());
            // other way
            // Optional<User> savedUser = userRepository.findById(user.getId());
            if (savedUser == null) {
                return false; // User not found
            }

            // Update user information
            savedUser.setId(id);
            savedUser.setName(user.getName());
            savedUser.setPassword(user.getPassword());
            savedUser.setPhone(user.getPhone());
            savedUser.setRole(user.getRole());

            userRepository.save(savedUser);
            return true; // User updated successfully
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while updating user", e);
        }
    }


    // get All User
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            userDtos.add(new UserDto(user));
        }
        return userDtos;
    }

    // find One User
    public Optional<UserDto> findOneUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.map(UserDto::new);
    }


    // delete one user by id
    @Transactional
    public void deleteUserById(Long userId) {
        // Delete orders associated with the user
        orderRepository.deleteByUserId(userId);

        // Delete authentication tokens associated with the user
        tokenRepository.deleteByUserId(userId);

        // Delete the user itself
        userRepository.deleteById(userId);
    }





//    public void deleteUserById(Long id) {
//
//        userRepository.deleteById(id);
//    }


}








// delete one user by id
//public boolean deleteUserById(User authUser, Long id) {
//
//    if (!authUser.getId().equals(id)) {
//        throw new ForbiddenException("Forbidden");
////            return false;
//    }
//    try {
//        userRepository.deleteById(id);
//        return true;
//    }
//    catch (DataAccessException e) {
//        throw new RuntimeException("Error occurred while deleting user", e);
//    }
//}








// findOneUser
//public Optional<UserDto> findOneUser(Long id) {
//    Optional<User> optionalUser = userRepository.findById(id);
//    if (optionalUser.isPresent()) {
//        return Optional.of(new UserDto(optionalUser.get()));
//    } else {
//        return Optional.empty();
//    }
//}