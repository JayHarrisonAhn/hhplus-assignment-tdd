package com.example.concert.token.consumer;

import com.example.concert.concert.domain.outboxconcertseatoccupy.ConcertSeatOccupyOutbox;
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

    @KafkaListener(topics = "concert.seat-occupy", groupId = "token")
    public void expireToken(String message) throws JsonProcessingException {
        ConcertSeatOccupyOutbox event = objectMapper.readValue(message, ConcertSeatOccupyOutbox.class);

        this.tokenFacade.expireToken(
                event.getEvent().tokenString()
        );
    }
}
