package io.hhplus.tdd.point.util;

import io.hhplus.tdd.point.dto.PointHistoryDTO;
import io.hhplus.tdd.point.dto.UserPointDTO;
import io.hhplus.tdd.point.entity.PointHistory;
import io.hhplus.tdd.point.entity.TransactionType;
import io.hhplus.tdd.point.entity.UserPoint;
import io.hhplus.tdd.point.repository.PointHistoryRepository;
import io.hhplus.tdd.point.repository.UserPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PointManager {
    final private UserPointRepository userPointRepository;
    final private PointHistoryRepository pointHistoryRepository;
    final private PointTableAccessor pointTableAccessor;

    public UserPoint getPoint(long userId) {
        return this.pointTableAccessor.access(userId, () ->
                this.userPointRepository.selectById(userId)
        );
    }

    public UserPoint accumulatePoint(long userId, long updateAmount, TransactionType transactionType) {
        return this.pointTableAccessor.access(userId, () -> {
            UserPoint userPoint = this.userPointRepository.selectById(userId);

            UserPointDTO updatedUserPointDTO = new UserPointDTO(
                    userPoint.id(),
                    userPoint.point() + updateAmount
            );
            updatedUserPointDTO.validate();

            UserPoint updatedUserPoint = this.userPointRepository.insertOrUpdate(updatedUserPointDTO);

            PointHistoryDTO pointHistoryDTO = new PointHistoryDTO(
                    updatedUserPoint.id(),
                    updateAmount,
                    transactionType,
                    updatedUserPoint.updateMillis()
            );
            this.pointHistoryRepository.insert(pointHistoryDTO);

            return updatedUserPoint;
        });
    }

    public List<PointHistory> getPointHistory(long userId) {
        return this.pointTableAccessor.access(userId, () ->
                this.pointHistoryRepository.selectAllByUserId(userId)
        );
    }
}
