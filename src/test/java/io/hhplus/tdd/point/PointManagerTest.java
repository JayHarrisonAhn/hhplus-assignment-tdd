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
        this.userId = 1;
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    long userId;

    @Test
    void incrementPoint_success() {
        // Given
        long pointAmount = 1000;
        long incrementAmount = 2000;
        UserPoint userPoint = new UserPoint(this.userId, pointAmount, 1);
        TransactionType transactionType = TransactionType.CHARGE;

        given(
                userPointTable.selectById(this.userId)
        ).willReturn(
                userPoint
        );

        // When
        this.pointManager.incrementPoint(this.userId, incrementAmount, transactionType);

        // Then
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> amountCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<TransactionType> transactionCaptor = ArgumentCaptor.forClass(TransactionType.class);

        then(userPointTable).should(atMostOnce()).insertOrUpdate(
                userIdCaptor.capture(),
                amountCaptor.capture()
        );
        assertEquals(this.userId, userIdCaptor.getValue());
        assertEquals(pointAmount + incrementAmount, amountCaptor.getValue());

        then(pointHistoryTable).should(atMostOnce()).insert(
                userIdCaptor.capture(),
                amountCaptor.capture(),
                transactionCaptor.capture(),
                anyLong()
        );
        assertEquals(this.userId, userIdCaptor.getValue());
        assertEquals(incrementAmount, amountCaptor.getValue());
        assertEquals(transactionType, transactionCaptor.getValue());
    }
}