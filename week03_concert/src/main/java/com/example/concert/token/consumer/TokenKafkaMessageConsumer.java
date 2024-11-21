package com.example.concert.token.consumer;

import com.example.concert.concert.event.ConcertSeatOccupyEvent;
import com.example.concert.token.TokenFacade;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenKafkaMessageConsumer {

    private final TokenFacade tokenFacade;

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "concert.seat-occupy", groupId = "group_id")
    public void expireToken(String message) throws JsonProcessingException {
        ConcertSeatOccupyEvent event = objectMapper.readValue(message, ConcertSeatOccupyEvent.class);
        event
                .tokenString()
                .ifPresent(this.tokenFacade::expireToken);
    }
}
