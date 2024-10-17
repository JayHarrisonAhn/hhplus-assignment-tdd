package com.example.concert.application.user;

import com.example.concert.application.balance.BalanceService;
import com.example.concert.application.token.TokenService;
import com.example.concert.domain.User;
import com.example.concert.domain.token.Token;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Transactional
public class UserFacade {

    private final UserService userService;
    private final BalanceService balanceService;

    public User createUser(String username) {
        User user = userService.createUser(username);
        balanceService.createBalance(user.getId());
        return user;
    }

    public User findByUsername(String username) {
        return userService.findByUsername(username);
    }
}
