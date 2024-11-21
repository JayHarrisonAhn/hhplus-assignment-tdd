package com.example.concert.concert.infra;

import com.example.concert.concert.domain.ConcertKafkaMessageProducer;
import com.example.concert.concert.domain.outboxconcertseatoccupy.ConcertSeatOccupyOutbox;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Primary
@Component
@RequiredArgsConstructor
public class ConcertKafkaMessageProducerImpl implements ConcertKafkaMessageProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendConcertSeatOccupyEvent(ConcertSeatOccupyOutbox concertSeatOccupyOutbox) {
        kafkaTemplate.send(
                "concert.seat-occupy",
                concertSeatOccupyOutbox.getEvent().seatId().toString(),
                concertSeatOccupyOutbox
        );
    }
}
