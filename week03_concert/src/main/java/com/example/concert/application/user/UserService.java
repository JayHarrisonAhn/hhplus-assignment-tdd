package com.example.concert.application.user;

import com.example.concert.application.user.repository.UserRepository;
import com.example.concert.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    User createUser(String username) {
        User user = User.builder()
                .username(username)
                .build();

        user.validate();

        return userRepository.save(user);
    }

    Optional<User> findByUserId(Long userId) {
        return userRepository.findById(userId);
    }
}
