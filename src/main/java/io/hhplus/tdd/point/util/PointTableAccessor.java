package io.hhplus.tdd.point.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@Component
@RequiredArgsConstructor
public class PointTableAccessor {
    private final ConcurrentHashMap<Long, Object> userLocks = new ConcurrentHashMap<>();

    public <T> T access (long userId, PointTableAccessOperator<T> operator) {
        AtomicReference<T> result = new AtomicReference<>();
        userLocks.compute(userId, (k, v) -> {
            result.set(operator.run());
            return v;
        });
        return result.get();
    }
}
