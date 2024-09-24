package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
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
    private UserPointTable userPointTable;

    @Mock
    private PointHistoryTable pointHistoryTable;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        this.closeable = MockitoAnnotations.openMocks(this);
        this.userPoint = new UserPoint(1, 1000, 1);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    UserPoint userPoint;

    @Test
    void incrementPoint_success() {
        // Given
        this.userPoint = new UserPoint(1, 1000, 1);
        long incrementAmount = 2000;

        given(
                userPointTable.selectById(this.userPoint.id())
        ).willReturn(
                userPoint
        );

        // When
        this.pointManager.incrementPoint(this.userPoint.id(), incrementAmount, TransactionType.CHARGE);

        // Then
        ArgumentCaptor<Long> amountCaptor = ArgumentCaptor.forClass(Long.class);

        then(userPointTable).should(atMostOnce()).insertOrUpdate(
                anyLong(),
                amountCaptor.capture()
        );
        assertEquals(this.userPoint.point() + incrementAmount, amountCaptor.getValue());

        then(pointHistoryTable).should(atMostOnce()).insert(
                anyLong(),
                amountCaptor.capture(),
                any(TransactionType.class),
                anyLong()
        );
        assertEquals(incrementAmount, amountCaptor.getValue());
    }

    @Test
    void incrementPoint_fail_minusTotalAmount() {
        // Given
        this.userPoint = new UserPoint(1, 1000, 1);
        long decrementAmount = -2000;

        given(
                userPointTable.selectById(this.userPoint.id())
        ).willReturn(
                userPoint
        );

        // When
        assertThrows(
                IllegalArgumentException.class,
                () -> this.pointManager.incrementPoint(this.userPoint.id(), decrementAmount, TransactionType.USE)
        );
    }
}