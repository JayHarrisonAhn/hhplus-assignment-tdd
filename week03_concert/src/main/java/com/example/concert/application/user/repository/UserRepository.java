package com.example.concert.application.user.repository;

import com.example.concert.domain.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UserRepository {

    public Optional<User> findById(Long id);
    public User save(User user);
}
