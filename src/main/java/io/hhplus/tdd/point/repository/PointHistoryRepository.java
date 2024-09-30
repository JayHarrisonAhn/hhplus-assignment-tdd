package io.hhplus.tdd.point.repository;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.point.dto.PointHistoryDTO;
import io.hhplus.tdd.point.entity.PointHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Repository
@RequiredArgsConstructor
public class PointHistoryRepository {

    final private PointHistoryTable pointHistoryTable;

    final private ReadWriteLock tableLock = new ReentrantReadWriteLock();

    public PointHistory insert(PointHistoryDTO pointHistoryDTO) {
        Lock lock = tableLock.writeLock();
        lock.lock();
        PointHistory pointHistory = this.pointHistoryTable.insert(
                pointHistoryDTO.userId(),
                pointHistoryDTO.amount(),
                pointHistoryDTO.type(),
                pointHistoryDTO.updateMillis()
        );
        lock.unlock();
        return pointHistory;
    }

    public List<PointHistory> selectAllByUserId(long userId) {
        Lock lock = tableLock.readLock();
        lock.lock();
        List<PointHistory> pointHistories = this.pointHistoryTable.selectAllByUserId(userId);
        lock.unlock();
        return pointHistories;
    }
}
