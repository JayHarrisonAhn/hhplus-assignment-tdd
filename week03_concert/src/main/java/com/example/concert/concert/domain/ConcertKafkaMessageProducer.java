package com.example.concert.concert.domain;

import com.example.concert.concert.domain.outboxconcertseatoccupy.ConcertSeatOccupyOutbox;
import org.springframework.stereotype.Component;

@Component
public interface ConcertKafkaMessageProducer {

    void sendConcertSeatOccupyEvent(ConcertSeatOccupyOutbox concertSeatOccupyOutbox);
}
