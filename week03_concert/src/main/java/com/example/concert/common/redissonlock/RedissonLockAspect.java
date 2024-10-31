package com.example.concert.common.redissonlock;

import com.example.concert.common.error.CommonErrorCode;
import com.example.concert.common.error.CommonException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Aspect
@Component
@Order(1)
@RequiredArgsConstructor
public class RedissonLockAspect {

    private final RedissonClient redissonClient;
    private final TransactionTemplate transactionTemplate;

    @Around("@annotation(redissonLock)")
    public Object redissonLock(ProceedingJoinPoint joinPoint, RedissonLock redissonLock) throws Throwable {
        String lockKey = this.replaceWithArgs(redissonLock.value(), joinPoint);
        System.out.println(lockKey);

        long waitTime = redissonLock.waitTime();
        long leaseTime = redissonLock.leaseTime();

        RLock lock = redissonClient.getLock(lockKey);
        boolean isLocked = false;

        try {
            isLocked = lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
            if (isLocked) {
                return joinPoint.proceed();
            } else {
                throw new CommonException(CommonErrorCode.TEMPORARILY_UNAVAILABLE);
            }
        } finally {
            if (isLocked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private String replaceWithArgs(String originalKey, ProceedingJoinPoint joinPoint) {
        String newKey = originalKey;
        List<String> args = requiredArgs(originalKey);
        for (String arg : args) {
            newKey = newKey.replace(
                    "{"+arg+"}",
                    Objects.requireNonNull(findArg(joinPoint, arg)).toString()
            );
        }
        return newKey;
    }

    private List<String> requiredArgs(String key) {
        Pattern pattern = Pattern.compile("\\{([^}]+)\\}");
        Matcher matcher = pattern.matcher(key);

        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(matcher.group(1));
        }
        return list;
    }

    private Object findArg(ProceedingJoinPoint joinPoint, String argName) {
        String[] parameterNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < parameterNames.length; i++) {
            if (parameterNames[i].equals(argName)) {
                return args[i];
            }
        }
        return null;
    }
}
