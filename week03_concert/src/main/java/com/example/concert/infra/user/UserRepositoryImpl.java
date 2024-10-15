package com.example.concert.infra.user;

import com.example.concert.application.user.repository.UserRepository;
import com.example.concert.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Primary
@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserRepositoryJPA userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
