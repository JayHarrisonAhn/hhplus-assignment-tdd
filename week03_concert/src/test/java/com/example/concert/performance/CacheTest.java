package com.example.concert.performance;

import com.example.concert.TestEnv;
import com.example.concert.common.error.CommonException;
import com.example.concert.token.TokenFacade;
import com.example.concert.user.UserFacade;
import com.example.concert.user.domain.User;
import groovy.util.logging.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

@Slf4j
public class CacheTest extends TestEnv {

    private static final Logger log = LoggerFactory.getLogger(CacheTest.class);
    @Autowired private UserFacade userFacade;
    @Autowired private TokenFacade tokenFacade;

    @Test
    void token_100Users() {
        // Given
        int numOfUsers = 1000;
        List<User> users = IntStream.range(0, numOfUsers)
                .mapToObj( i -> userFacade.createUser(String.valueOf(i)))
                .toList();

        // When
        long startTime = System.currentTimeMillis();
        List<CompletableFuture<String>> tasks = IntStream.range(0, numOfUsers)
                .<CompletableFuture<String>>mapToObj( i -> CompletableFuture.supplyAsync(() -> {
                    Long userId = users.get(i).getId();

                    UUID token = tokenFacade.issue(userId).getToken();
                    while (true) {
                        try {
                            tokenFacade.refreshTokenQueue(1);
                            tokenFacade.check(userId, token.toString());
                            break;
                        } catch (Exception ignored) {
                            log.info(ignored.getMessage());
                        }

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    return null;
                })).toList();
        CompletableFuture
                .allOf(tasks.toArray(new CompletableFuture[0]))
                .join();
        long endTime = System.currentTimeMillis();

        // Then
        log.info("Total time: {} ms", endTime - startTime);
    }
}
