package io.hhplus.tdd.point.repository;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.point.dto.PointHistoryDTO;
import io.hhplus.tdd.point.entity.PointHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PointHistoryRepository {

    final private PointHistoryTable pointHistoryTable;

    public PointHistory insert(PointHistoryDTO pointHistory) {
        return this.pointHistoryTable.insert(
                pointHistory.userId(),
                pointHistory.amount(),
                pointHistory.type(),
                pointHistory.updateMillis()
        );
    }

    public List<PointHistory> selectAllByUserId(long userId) {
        return this.pointHistoryTable.selectAllByUserId(userId);
    }
}
