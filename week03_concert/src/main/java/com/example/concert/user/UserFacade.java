package com.example.concert.user;

import com.example.concert.balance.BalanceService;
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
