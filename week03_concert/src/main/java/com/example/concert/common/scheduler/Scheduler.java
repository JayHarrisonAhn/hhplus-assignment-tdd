package com.example.concert.common.scheduler;

import com.example.concert.token.TokenFacade;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@Transactional
@Slf4j
public class Scheduler {

    public Scheduler(TokenFacade tokenFacade) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable task = () -> {
            tokenFacade.refreshTokenQueue(5);
            log.info("Token refreshed");
        };

        scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
    }
}
