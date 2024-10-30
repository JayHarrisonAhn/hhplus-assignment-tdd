package com.example.concert.concert.domain.concerttimeslotoccupancy;

import com.example.concert.common.error.CommonErrorCode;
import com.example.concert.common.error.CommonException;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcertTimeslotOccupancy {

    @Id
    private Long concertTimeslotId;

    private Integer maxSeatAmount;

    private Integer occupiedSeatAmount;

    public void increaseMaxSeatAmount(Integer amount) {
        this.maxSeatAmount += amount;
    }

    public void increaseOccupiedSeatAmount() {
        if (occupiedSeatAmount >= maxSeatAmount) {
            throw new CommonException(CommonErrorCode.CONCERT_SEAT_ALREADY_OCCUPIED);
        }
        this.occupiedSeatAmount += 1;
    }
}
