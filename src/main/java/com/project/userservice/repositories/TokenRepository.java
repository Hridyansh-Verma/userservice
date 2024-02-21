package com.project.userservice.repositories;

import com.project.userservice.models.Token;
import com.project.userservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Token save(Token token);

    Optional<Token> findByValue(String token);
    Optional<Token> findByValueAndIsDeletedEqualsAndExpiryAtGreaterThan(String token, Boolean isDeleted, Date expiryAt);
}
