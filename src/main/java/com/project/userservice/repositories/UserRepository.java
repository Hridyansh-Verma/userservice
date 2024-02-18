package com.project.userservice.repositories;

import com.project.userservice.dtos.LoginRequestDto;
import com.project.userservice.models.User;
import com.project.userservice.service.UserService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User save(User user);
    Optional<User> findByEmail(String email);
}
