package com.example.concert.token.event;

import com.example.concert.concert.event.ConcertSeatOccupyEvent;
import com.example.concert.token.TokenFacade;
import com.example.concert.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class TokenEventHandler {

    private final TokenFacade tokenFacade;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void expireToken(ConcertSeatOccupyEvent concertSeatOccupyEvent) {
        concertSeatOccupyEvent
                .tokenString()
                .ifPresent(this.tokenFacade::expireToken);
    }
}
