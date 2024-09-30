package io.hhplus.tdd.point.repository;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.dto.UserPointDTO;
import io.hhplus.tdd.point.entity.UserPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Repository
@RequiredArgsConstructor
public class UserPointRepository {

    final private UserPointTable userPointTable;

    final private ReadWriteLock tableLock = new ReentrantReadWriteLock();

    public UserPoint selectById(Long id) {
        Lock lock = tableLock.readLock();
        lock.lock();
        UserPoint userPoint = userPointTable.selectById(id);
        lock.unlock();
        return userPoint;
    }

    public UserPoint insertOrUpdate(UserPointDTO userPointDTO) {
        Lock lock = tableLock.writeLock();
        lock.lock();
        UserPoint userPoint = userPointTable.insertOrUpdate(
                userPointDTO.id(),
                userPointDTO.point()
        );
        lock.unlock();
        return userPoint;
    }
}
