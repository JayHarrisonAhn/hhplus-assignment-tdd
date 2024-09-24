package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointService {
    @Autowired
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
        return this.pointManager.incrementPoint(userId, amount, TransactionType.CHARGE);
    }

    public UserPoint usePoint(long userId, long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException();
        }
        return this.pointManager.incrementPoint(userId, -amount, TransactionType.USE);
    }
}
