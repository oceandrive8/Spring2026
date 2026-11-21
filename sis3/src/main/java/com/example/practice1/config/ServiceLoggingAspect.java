package com.example.practice1.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ServiceLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(ServiceLoggingAspect.class);

    @Around("execution(public * com.example.practice1.movie.app.MovieService.*(..))")
    public Object logExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String method = joinPoint.getSignature().toShortString();
        log.info("-> {} args={}", method, Arrays.toString(joinPoint.getArgs()));

        long start = System.nanoTime();
        try {
            Object result = joinPoint.proceed();
            log.info("<- {} OK in {} ms", method, elapsedMs(start));
            return result;
        } catch (Throwable ex) {
            log.warn("<- {} FAILED in {} ms: {}: {}",
                    method, elapsedMs(start), ex.getClass().getSimpleName(), ex.getMessage());
            throw ex;
        }
    }

    private static long elapsedMs(long startNanos) {
        return (System.nanoTime() - startNanos) / 1_000_000;
    }
}
