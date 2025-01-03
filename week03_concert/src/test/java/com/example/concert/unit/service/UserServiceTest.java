package com.example.concert.unit.service;

import com.example.concert.common.error.CommonException;
import com.example.concert.user.UserService;
import com.example.concert.user.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("성공 : normal username")
    void success_build() {
        assertDoesNotThrow( () -> {
            this.userService.createUser("test user aaa");
        });
    }

    @Test
    @DisplayName("실패 : null username")
    void fail_null_username() {
        assertThrows(
                CommonException.class,
                () -> {
                    this.userService.createUser(null);
                }
        );
    }

    @Test
    @DisplayName("실패 : empty username")
    void fail_empty_username() {
        assertThrows(
                CommonException.class,
                () -> {
                    this.userService.createUser("");
                }
        );
    }
}