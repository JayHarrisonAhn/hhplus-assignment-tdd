package com.example.concert.application.concert;

import com.example.concert.domain.dto.ConcertSeatPayInfo;
import com.example.concert.domain.dto.ConcertTimeslotWithOccupancy;
import com.example.concert.application.balance.BalanceService;
import com.example.concert.application.token.TokenService;
import com.example.concert.application.user.UserService;
import com.example.concert.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ConcertFacade {

    private final ConcertService concertService;
    private final UserService userService;
    private final BalanceService balanceService;
    private final TokenService tokenService;

    public List<ConcertTimeslotWithOccupancy> findConcertTimeslots(Long concertId) {
        Concert concert = concertService.findConcert(concertId);

        return concertService.findConcertTimeslots(concert.getId());
    }

    public List<ConcertSeat> findConcertSeats(Long timeslotId) {
        ConcertTimeslot timeslot = concertService.findConcertTimeslot(timeslotId);

        return concertService.findConcertSeats(timeslot.getId());
    }

    public ConcertSeat occupyConcertSeat(Long seatId, Long userId) {
        User user = userService.findByUserId(userId);

        ConcertSeat seat = concertService.findConcertSeat(seatId);

        seat.occupy(user.getId(), LocalDateTime.now());

        return seat;
    }

    public ConcertSeatPayInfo paySeat(Long seatId, Long userId) {
        ConcertSeat seat = this.concertService.findConcertSeat(seatId);

        BalanceHistory balanceHistory = this.balanceService.pay(userId, seat.getPrice());

        seat.pay(balanceHistory.getId());

        return ConcertSeatPayInfo.builder()
                .seat(seat)
                .balanceHistory(balanceHistory)
                .build();
    }
}
