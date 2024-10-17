package com.example.concert.application.concert;

import com.example.concert.application.concert.dto.ConcertTimeslotWithOccupancy;
import com.example.concert.application.token.TokenService;
import com.example.concert.application.user.UserService;
import com.example.concert.domain.Concert;
import com.example.concert.domain.ConcertSeat;
import com.example.concert.domain.ConcertTimeslot;
import com.example.concert.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ConcertFacade {

    private final ConcertService concertService;
    private final UserService userService;
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
}
