package com.example.concert.common.scheduler;

import com.example.concert.token.TokenFacade;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
@Slf4j
@RequiredArgsConstructor
public class Scheduler {

    private TokenFacade tokenFacade;

    @Scheduled(fixedDelay = 1000)
    private void tokenScheduler() {
        tokenFacade.refreshTokenQueue(5);
        log.info("Token refreshed");
    }
}
