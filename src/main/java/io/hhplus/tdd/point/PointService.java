package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointService {
    @Autowired
    private UserPointTable userPointTable;

    @Autowired
    private PointHistoryTable pointHistoryTable;

    public UserPoint viewPointAmount(long userId) {
        UserPoint userPoint = userPointTable.selectById(userId);
        return userPoint;
    }

    public List<PointHistory> viewPointHistory(long userId) {
        List<PointHistory> pointHistories = pointHistoryTable.selectAllByUserId(userId);
        return pointHistories;
    }

    public UserPoint chargePoint(long userId, long amount) {
        UserPoint userPoint = userPointTable.selectById(userId);
        return userPointTable.insertOrUpdate(userId, userPoint.point() + amount);
    }

    public UserPoint usePoint(long userId, long amount) {
        UserPoint userPoint = userPointTable.selectById(userId);
        return userPointTable.insertOrUpdate(userId, userPoint.point() - amount);
    }
}
