package com.example.concert.user;

import com.example.concert.common.error.CommonErrorCode;
import com.example.concert.common.error.CommonException;
import com.example.concert.user.domain.User;
import com.example.concert.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(String username) {
        User user = User.builder()
                .username(username)
                .build();

        user.validate();

        return userRepository.save(user);
    }

    public User findByUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(CommonErrorCode.USER_NOT_FOUND));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CommonException(CommonErrorCode.USER_NOT_FOUND));
    }
}
