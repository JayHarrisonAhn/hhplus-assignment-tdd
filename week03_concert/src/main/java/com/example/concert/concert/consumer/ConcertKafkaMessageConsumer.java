package com.example.concert.concert.consumer;

import com.example.concert.concert.ConcertFacade;
import com.example.concert.concert.domain.outboxconcertseatoccupy.ConcertSeatOccupyOutbox;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConcertKafkaMessageConsumer {

    private final ObjectMapper objectMapper;
    private final ConcertFacade concertFacade;

    @Transactional
    @KafkaListener(topics = "concert.seat-occupy", groupId = "concert")
    public void checkPublishedEvent(String message) throws JsonProcessingException {
        System.out.println("Concert Consumer Running");
        ConcertSeatOccupyOutbox event = objectMapper.readValue(message, ConcertSeatOccupyOutbox.class);

        concertFacade.checkOutboxPublished(event.getId());
    }
}
