package com.example.concert.concert.domain;

import com.example.concert.concert.event.ConcertSeatOccupyEvent;
import org.springframework.stereotype.Component;

@Component
public interface ConcertKafkaMessageProducer {
    void sendConcertSeatOccupyEvent(ConcertSeatOccupyEvent concertSeatOccupyEvent);
}
