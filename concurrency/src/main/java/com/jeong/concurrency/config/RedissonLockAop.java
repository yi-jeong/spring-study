package com.jeong.concurrency.config;

import com.jeong.concurrency.util.CustomSpringELParser;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
public class RedissonLockAop {
    private static final String REDISSON_LOCK_PREFIX = "LOCK:";
    private static final Logger logger = LoggerFactory.getLogger(RedissonLockAop.class);

    private final RedissonClient redissonClient;

    @Around("@annotation(com.jeong.concurrency.config.RedissonLock)")
    public Object lock(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RedissonLock redissonLock = method.getAnnotation(RedissonLock.class);

        String key = REDISSON_LOCK_PREFIX + CustomSpringELParser.getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), redissonLock.key());
        RLock rLock = redissonClient.getLock(key);
        boolean available = false;

        try {
            available = rLock.tryLock(redissonLock.waitTime(), redissonLock.leaseTime(), redissonLock.timeUnit());
            if (!available) {
                logger.info("락 수행 실패: {}", key);
                return null;
            }
            logger.info("락 수행: {}", key);
            return joinPoint.proceed();
        } catch (InterruptedException e) {
            logger.error("에러임: {}", key, e);
            throw new RuntimeException("Lock acquisition interrupted", e);
        } finally {
            if (available) {
                rLock.unlock();
                logger.info("Lock released on key: {}", key);
            }
        }
    }
}