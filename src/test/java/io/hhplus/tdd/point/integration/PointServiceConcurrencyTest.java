package io.hhplus.tdd.point.integration;

import io.hhplus.tdd.point.entity.PointHistory;
import io.hhplus.tdd.point.entity.UserPoint;
import io.hhplus.tdd.point.service.PointService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.lang.reflect.Executable;
import java.util.*;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

interface UserCallable {
    void call(long userId);
}

@SpringBootTest
public class PointServiceConcurrencyTest {

    @Autowired
    private PointService pointService;

    int nThreads = 10;
    int numOfUsers = 5;
    int executionTimePerUser = 5;

    @BeforeEach
    void setUp() {

    }

    @Test
    @DirtiesContext
    @DisplayName("여러 User에게 여러 charge 요청 성공")
    void concurrency_success_multiple_charge() {
        // Given
        long chargeAmount = 100;

        // When
        assertDoesNotThrow( () -> {
            executeRepeatPerUser((UserCallable) (userId) -> {
                pointService.chargePoint(userId, chargeAmount);
            });
        });

        // Then
        validatePointAmountAndHistories(executionTimePerUser * chargeAmount);
    }

    @Test
    @DirtiesContext
    @DisplayName("여러 User에게 여러 charge+use 요청 성공")
    void concurrency_success_multiple_charge_use() {
        // Given
        long chargeAmount = 100;
        for (int userId = 1; userId <= numOfUsers; userId++) {
            this.pointService.chargePoint(userId, chargeAmount * executionTimePerUser);
        }

        // When
        assertDoesNotThrow( () -> {
            executeRepeatPerUser((UserCallable) (userId ) -> {
                pointService.chargePoint(userId, chargeAmount);
            }, ( userId ) -> {
                pointService.usePoint(userId, chargeAmount);
            });
        });

        // Then
        validatePointAmountAndHistories(executionTimePerUser * chargeAmount);
    }

    @Test
    @DisplayName("여러 User에게 잔고 이상 출금 시도 실패")
    @DirtiesContext
    void concurrency_fail_withdraw_more_than_amount() {
        // Given
        long withdrawAmount = 100;
        for (int userId = 1; userId <= numOfUsers; userId++) {
            this.pointService.chargePoint(userId, withdrawAmount * (executionTimePerUser - 1));
        }

        // When
        assertDoesNotThrow( () -> {
            executeRepeatPerUser((UserCallable) (userId ) -> {
                assertThrows(IllegalStateException.class, () -> pointService.usePoint(userId, withdrawAmount));
            });
        });
    }

    private void executeRepeatPerUser(UserCallable... callables) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        CountDownLatch latch = new CountDownLatch(numOfUsers * executionTimePerUser);
        Set<Callable<Void>> callableSet = new HashSet<>();

        for (int i = 0; i < executionTimePerUser; i++) {
            for (long userId = 1; userId <= numOfUsers; userId++) {
                long finalUserId = userId;
                for (UserCallable callable : callables) {
                    callableSet.add( () -> {
                        Exception exception = null;
                        try {
                            callable.call(finalUserId);
                        } catch (Exception e) {
                            exception = e;
                        } finally {
                            latch.countDown();
                        }
                        if (exception != null) {
                            throw exception;
                        }
                        return null;
                    });
                }
            }
        }

        executorService.invokeAll(callableSet);
        latch.await();
        executorService.shutdown();
    }

    private void validatePointAmountAndHistories(long expectedAmount) {
        for (long userId = 1; userId <= numOfUsers; userId++) {
            UserPoint userPoint = this.pointService.viewPointAmount(userId);
            assertEquals(expectedAmount, userPoint.point());

            List<PointHistory> pointHistories = this.pointService.viewPointHistory(userId);
            long totalAmount = pointHistories.stream().mapToLong(PointHistory::amount).sum();
            assertEquals(expectedAmount, totalAmount);
        }
    }
}
