package com.example.concert.concert.domain.concerttimeslotoccupancy;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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

    public void increaseOccupiedSeatAmount(Integer amount) {
        this.maxSeatAmount += amount;
    }
}
