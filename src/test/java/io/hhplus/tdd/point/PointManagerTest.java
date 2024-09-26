package io.hhplus.tdd.point;

import io.hhplus.tdd.point.dto.PointHistoryDTO;
import io.hhplus.tdd.point.dto.UserPointDTO;
import io.hhplus.tdd.point.entity.TransactionType;
import io.hhplus.tdd.point.entity.UserPoint;
import io.hhplus.tdd.point.repository.PointHistoryRepository;
import io.hhplus.tdd.point.repository.UserPointRepository;
import io.hhplus.tdd.point.util.PointManager;
import io.hhplus.tdd.point.util.PointTableAccessOperator;
import io.hhplus.tdd.point.util.PointTableAccessor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

class PointManagerTest {
    @InjectMocks
    private PointManager pointManager;

    @Mock
    private UserPointRepository userPointRepository;

    @Mock
    private PointHistoryRepository pointHistoryRepository;

    @Mock
    private PointTableAccessor pointTableAccessor;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        this.closeable = MockitoAnnotations.openMocks(this);
        this.userPoint = new UserPoint(1, 1000, 1);
        doAnswer( invocation -> {
            PointTableAccessOperator operator = invocation.getArgument(1);
            return operator.run();
        }).when(this.pointTableAccessor).access(anyLong(), any(PointTableAccessOperator.class));
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    UserPoint userPoint;

    @Test
    void accumulatePoint_success() {
        // Given
        this.userPoint = new UserPoint(1, 1000, 1);
        long incrementAmount = 2000;
        TransactionType transactionType = TransactionType.CHARGE;

        given(
                userPointRepository.selectById(this.userPoint.id())
        ).willReturn(
                userPoint
        );

        doAnswer( invocationOnMock -> {
            UserPointDTO userPointDTO = invocationOnMock.getArgument(0);
            return new UserPoint(
                    userPointDTO.id(),
                    userPointDTO.point(),
                    System.currentTimeMillis()
            );
        }).when(this.userPointRepository).insertOrUpdate(any(UserPointDTO.class));

        // When
        UserPoint accumulatedPoint = this.pointManager.accumulatePoint(
                this.userPoint.id(),
                incrementAmount,
                transactionType
        );
        assertNotNull(accumulatedPoint);

        // Then
        ArgumentCaptor<UserPointDTO> userPointDTOCaptor = ArgumentCaptor.forClass(UserPointDTO.class);
        then(userPointRepository)
                .should(atMostOnce())
                .insertOrUpdate(
                        userPointDTOCaptor.capture()
                );
        assertEquals(this.userPoint.id(), userPointDTOCaptor.getValue().id());
        assertEquals(this.userPoint.point() + incrementAmount, userPointDTOCaptor.getValue().point());

        ArgumentCaptor<PointHistoryDTO> pointHistoryDTOCaptor = ArgumentCaptor.forClass(PointHistoryDTO.class);
        then(pointHistoryRepository)
                .should(atMostOnce())
                .insert(
                        pointHistoryDTOCaptor.capture()
                );
        assertEquals(this.userPoint.id(), pointHistoryDTOCaptor.getValue().userId());
        assertEquals(incrementAmount, pointHistoryDTOCaptor.getValue().amount());
        assertEquals(transactionType, pointHistoryDTOCaptor.getValue().type());

        assertEquals(accumulatedPoint.updateMillis(), pointHistoryDTOCaptor.getValue().updateMillis());
    }

    @Test
    void accumulatePoint_fail_minusTotalAmount() {
        // Given
        this.userPoint = new UserPoint(1, 1000, 1);
        long decrementAmount = -2000;

        given(
                userPointRepository.selectById(this.userPoint.id())
        ).willReturn(
                userPoint
        );

        // When
        assertThrows(
                IllegalStateException.class,
                () -> this.pointManager.accumulatePoint(
                        this.userPoint.id(),
                        decrementAmount,
                        TransactionType.USE
                )
        );
    }
}