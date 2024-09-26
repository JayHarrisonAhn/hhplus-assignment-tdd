package io.hhplus.tdd.point.service;

import io.hhplus.tdd.point.util.PointManager;
import io.hhplus.tdd.point.entity.PointHistory;
import io.hhplus.tdd.point.entity.TransactionType;
import io.hhplus.tdd.point.entity.UserPoint;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@NoArgsConstructor
@Service
public class PointService {
    private PointManager pointManager;

    public UserPoint viewPointAmount(long userId) {
        return this.pointManager.getPoint(userId);
    }

    public List<PointHistory> viewPointHistory(long userId) {
        return this.pointManager.getPointHistory(userId);
    }

    public UserPoint chargePoint(long userId, long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException();
        }
        return this.pointManager.accumulatePoint(userId, amount, TransactionType.CHARGE);
    }

    public UserPoint usePoint(long userId, long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException();
        }
        return this.pointManager.accumulatePoint(userId, -amount, TransactionType.USE);
    }
}
