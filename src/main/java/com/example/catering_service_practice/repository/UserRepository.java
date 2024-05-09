package com.example.catering_service_practice.repository;

import com.example.catering_service_practice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailIgnoreCaseAndPassword(String email, String password);
    User findByEmail(String email);
    User getUserById(Long id);
}
