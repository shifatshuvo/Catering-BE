package com.example.catering_service_practice.repository;


import com.example.catering_service_practice.model.auth.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<AuthToken, Long> {
    Optional<AuthToken> findByTokenStr(String token);
    void deleteByUserId(Long userId);
}
