package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PointManager {
    @Autowired
    private UserPointTable userPointTable;

    @Autowired
    private PointHistoryTable pointHistoryTable;

    UserPoint getPoint(long userId) {
        return this.userPointTable.selectById(userId);
    }

    UserPoint incrementPoint(long userId, long amount, TransactionType transactionType) {
        UserPoint userPoint = this.userPointTable.selectById(userId);

        long totalAmount = userPoint.point() + amount;

        if(totalAmount < 0) {
            throw new IllegalArgumentException("Point amount must be greater than zero");
        }

        UserPoint newUserPoint = this.userPointTable.insertOrUpdate(userId, totalAmount);

        this.pointHistoryTable.insert(userId, amount, transactionType, System.currentTimeMillis());

        return newUserPoint;
    }

    List<PointHistory> getPointHistory(long userId) {
        return this.pointHistoryTable.selectAllByUserId(userId);
    }
}
