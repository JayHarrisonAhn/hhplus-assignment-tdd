package com.example.concert.token.infra;

import com.example.concert.token.domain.Token;
import com.example.concert.token.domain.TokenRepository;
import com.example.concert.token.domain.TokenStatus;
import org.redisson.api.RMap;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

@Primary
@Component
//public class TokenRepositoryRedisImpl {
public class TokenRepositoryRedisImpl implements TokenRepository {

    public TokenRepositoryRedisImpl(RedissonClient redissonClient) {
        this.tokenMap = redissonClient.getMap("token");
        this.waitingSet = redissonClient.getScoredSortedSet("token:wait");
        this.activeSet = redissonClient.getScoredSortedSet("token:active");
    }

    private final RMap<String, Token> tokenMap;
    private final RScoredSortedSet<String> waitingSet;
    private final RScoredSortedSet<String> activeSet;

    public Token save(Token token) {
        long timeStamp = dateTimeToTimestamp(token.getUpdatedAt());
        switch (token.getStatus()) {
            case WAIT:
                tokenMap.put(token.getToken().toString(), token);
                waitingSet.add(timeStamp, token.getToken().toString());
                activeSet.remove(token.getToken().toString());
                break;
            case ACTIVE:
                tokenMap.put(token.getToken().toString(), token);
                activeSet.add(timeStamp, token.getToken().toString());
                waitingSet.remove(token.getToken().toString());
                break;
            case EXPIRED:
                tokenMap.remove(token.getToken().toString());
                waitingSet.remove(token.getToken().toString());
                activeSet.remove(token.getToken().toString());
        }
        return token;
    }

    public Optional<Token> findByToken(UUID token) {
        return Optional.ofNullable(tokenMap.get(token.toString()));
    }

    public Optional<Token> findTopByStatusEqualsOrderByCreatedAtDesc(TokenStatus status) {
        switch (status) {
            case WAIT:
                String waitTokenString = waitingSet.pollFirst();
                if (waitTokenString == null) {
                    break;
                }
                Token waitToken = tokenMap.get(waitTokenString);
                return Optional.ofNullable(waitToken);
            case ACTIVE:
                String activeTokenString = activeSet.pollFirst();
                if (activeTokenString == null) {
                    break;
                }
                Token activeToken = tokenMap.get(activeTokenString);
                return Optional.ofNullable(activeToken);
            case EXPIRED:
                return Optional.empty();
        }
        return Optional.empty();
    }

    private long dateTimeToTimestamp(LocalDateTime dateTime) {
        ZoneOffset offset = ZoneOffset.UTC;
        return dateTime.toEpochSecond(offset);
    }
}
