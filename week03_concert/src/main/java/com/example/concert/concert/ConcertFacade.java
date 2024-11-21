package com.example.concert.concert;

import com.example.concert.balance.domain.balancehistory.BalanceHistory;
import com.example.concert.concert.domain.concert.Concert;
import com.example.concert.concert.domain.concertseat.ConcertSeat;
import com.example.concert.concert.domain.concerttimeslot.ConcertTimeslot;
import com.example.concert.concert.domain.concerttimeslotoccupancy.ConcertTimeslotOccupancy;
import com.example.concert.concert.domain.outboxconcertseatoccupy.ConcertSeatOccupyOutbox;
import com.example.concert.concert.dto.ConcertSeatPayInfo;
import com.example.concert.concert.dto.ConcertTimeslotWithOccupancy;
import com.example.concert.balance.BalanceService;
import com.example.concert.user.UserService;
import com.example.concert.user.domain.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
public class ConcertFacade {

    private final ConcertService concertService;
    private final UserService userService;
    private final BalanceService balanceService;

    public Concert createConcert(String name) {
        return concertService.createConcert(name);
    }

    public ConcertTimeslot createConcertTimeslot(Long concertId, LocalDateTime concertStartTime, LocalDateTime reservationStartTime) {
        return concertService.createConcertTimeslot(concertId, concertStartTime, reservationStartTime);
    }

    public List<ConcertSeat> createConcertSeats(Long concertTimeslotId, Long price, List<String> seatIds) {
        return concertService.createConcertSeats(concertTimeslotId, price, seatIds);
    }

    @Cacheable(value = "concert:timeslot", key = "#concertId", cacheManager = "redisCacheManager")
    public List<ConcertTimeslotWithOccupancy> findConcertTimeslots(Long concertId) {
        Concert concert = concertService.findConcert(concertId);

        return concertService.findConcertTimeslots(concert.getId());
    }

    public List<ConcertSeat> findConcertSeats(Long timeslotId) {
        ConcertTimeslot timeslot = concertService.findConcertTimeslot(timeslotId);

        return concertService.findConcertSeats(timeslot.getId());
    }

    public ConcertSeat occupyConcertSeat(Long seatId, Long userId, String tokenString) {
        User user = userService.findByUserId(userId);

        ConcertSeat seat = concertService.findConcertSeat(seatId);
        seat.occupy(user.getId(), LocalDateTime.now());

        ConcertTimeslotOccupancy timeslotOccupancy = concertService.findConcertTimeslotOccupancy(seat.getConcertTimeslotId());
        timeslotOccupancy.increaseOccupiedSeatAmount();

        ConcertSeatOccupyOutbox outbox = concertService.createOutbox(
                new ConcertSeatOccupyOutbox.ConcertSeatOccupyEvent(seatId, userId, tokenString)
        );

        try {
            concertService.produceKafkaMessage(outbox);
        } catch (Exception ignored) {}

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

    public void checkOutboxPublished(Long outboxId) {
        ConcertSeatOccupyOutbox outbox = concertService.findConcertSeatOccupyOutbox(outboxId);

        outbox.setPublished();
    }

    public void produceOldAndUnproducedOutboxes(LocalDateTime time) {
        List<ConcertSeatOccupyOutbox> outboxes = concertService.findUnproducedOutboxesBefore(time);

        outboxes.forEach( outbox -> {
            try {
                concertService.produceKafkaMessage(outbox);
                outbox.setPublished();
            } catch (Exception ignored) {}
        });
    }
}
