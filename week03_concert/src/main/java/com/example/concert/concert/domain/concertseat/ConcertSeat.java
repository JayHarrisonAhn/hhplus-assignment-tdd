package com.example.concert.concert.domain.concertseat;

import com.example.concert.common.error.CommonErrorCode;
import com.example.concert.common.error.CommonException;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"concertTimeslotId", "seatId"})
})
public class ConcertSeat {

    @Id
    @GeneratedValue
    Long id;

    @Column(name = "concertTimeslotId")
    Long concertTimeslotId;

    @Column(name = "seatId")
    String seatId;

    Long price;

    @Nullable
    Long userId;

    @Nullable
    Long payHistoryId;

    @Nullable
    LocalDateTime occupiedAt;

    @Version
    Integer version;

    public void occupy(Long userId, LocalDateTime occupiedAt) {
        if (this.occupiedAt != null && this.occupiedAt.plusMinutes(10).isAfter(LocalDateTime.now())) {
            throw new CommonException(CommonErrorCode.CONCERT_SEAT_ALREADY_OCCUPIED);
        }
        this.userId = userId;
        this.occupiedAt = occupiedAt;
    }

    public void pay(Long payHistoryId) {
        this.payHistoryId = payHistoryId;
    }
}
