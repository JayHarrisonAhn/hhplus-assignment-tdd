package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.entity.TransactionType;
import io.hhplus.tdd.point.entity.UserPoint;
import io.hhplus.tdd.point.service.PointService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PointServiceIntegrationTest {

    @Autowired
    private PointService pointService;

    int nThreads = 10;

    @Test
    void concurrency_success() {
        int numOfUsers = 10;
        int executionTimePerUser = 5;
        long chargeAmount = 100;

        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        CountDownLatch latch = new CountDownLatch(numOfUsers * executionTimePerUser);

        for (int userId = 1; userId <= numOfUsers; userId++) {
            for (int i = 0; i < executionTimePerUser; i++) {
                int finalUserId = userId;
                executorService.submit(() -> {
                    try {
                        pointService.chargePoint(finalUserId, chargeAmount);
                    } finally {
                        latch.countDown();
                    }
                });
            }
        }

        assertDoesNotThrow( () -> latch.await() );
        executorService.shutdown();

        for (int userId = 1; userId <= numOfUsers; userId++) {
            UserPoint userPoint = this.pointService.viewPointAmount(userId);
            assertEquals(executionTimePerUser * chargeAmount, userPoint.point());
        }
    }
}
