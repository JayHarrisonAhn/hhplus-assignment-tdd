package io.hhplus.tdd.point.repository;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.dto.UserPointDTO;
import io.hhplus.tdd.point.entity.UserPoint;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

@NoArgsConstructor
@Repository
public class UserPointRepository {

    private UserPointTable userPointTable;

    public UserPoint selectById(Long id) {
        return userPointTable.selectById(id);
    }

    public UserPoint insertOrUpdate(UserPointDTO userPoint) {
        return userPointTable.insertOrUpdate(
                userPoint.id(),
                userPoint.point()
        );
    }
}
