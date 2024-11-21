package com.example.concert.concert.domain.outboxconcertseatoccupy;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcertSeatOccupyOutbox {

    @Embeddable
    public record ConcertSeatOccupyEvent(
            Long seatId,
            Long userId,
            String tokenString
    ) { }

    public enum EventStatus {
        INIT, PUBLISHED
    }

    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private ConcertSeatOccupyEvent event;

    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;

    private LocalDateTime createdAt;

    public void setPublished() {
        eventStatus = EventStatus.PUBLISHED;
    }
}
