package com.example.concert.concert.infra;

import com.example.concert.concert.domain.ConcertKafkaMessageProducer;
import com.example.concert.concert.event.ConcertSeatOccupyEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Primary
@Component
@RequiredArgsConstructor
public class ConcertKafkaMessageProducerImpl implements ConcertKafkaMessageProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendConcertSeatOccupyEvent(ConcertSeatOccupyEvent concertSeatOccupyEvent) {
        kafkaTemplate.send(
                "concert.seat-occupy",
                concertSeatOccupyEvent.userId().toString(),
                concertSeatOccupyEvent
        );
    }
}
