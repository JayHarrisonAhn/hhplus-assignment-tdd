package com.example.concert.token.domain;

import com.example.concert.common.error.CommonErrorCode;
import com.example.concert.common.error.CommonException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token implements Serializable {

    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID token;

    @Column(unique = true)
    private Long userId;

    @Enumerated(EnumType.STRING)
    private TokenStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public void validateUser(Long userId) {
        if (!this.userId.equals(userId)) {
            throw new CommonException(CommonErrorCode.TOKEN_NOT_VALID);
        }
    }

    public void validateActive() {
        if (!this.status.equals(TokenStatus.ACTIVE)) {
            throw new CommonException(CommonErrorCode.TOKEN_NOT_VALID);
        }
    }

    public void activate(LocalDateTime updatedAt) {
        this.status = TokenStatus.ACTIVE;
        this.updatedAt = updatedAt;
    }
}
