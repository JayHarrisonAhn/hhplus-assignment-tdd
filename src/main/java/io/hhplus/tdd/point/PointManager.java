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

    UserPoint incrementPoint(long userId, long amount) {
        return new UserPoint(1,1,1);
    }

    List<PointHistory> getPointHistory(long userId) {
        return this.pointHistoryTable.selectAllByUserId(userId);
    }
}
