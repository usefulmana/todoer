package org.todoer.todoer.configs;

import com.google.firebase.messaging.FirebaseMessagingException;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class ResilienceConfig {

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)                     // 50% failure rate opens circuit
                .waitDurationInOpenState(Duration.ofSeconds(30))   // wait 30s before attempting again
                .permittedNumberOfCallsInHalfOpenState(3)         // test with 3 calls when half-open
                .minimumNumberOfCalls(5)                          // minimum calls before calculating rate
                .slidingWindowSize(10)                           // last 10 calls for statistics
                .recordExceptions(FirebaseMessagingException.class)
                .build();

        return CircuitBreakerRegistry.of(config);
    }

    @Bean
    public RateLimiterRegistry rateLimiterRegistry() {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitForPeriod(500)                         // 500 requests
                .limitRefreshPeriod(Duration.ofMinutes(1))   // per minute
                .timeoutDuration(Duration.ofSeconds(5))      // wait up to 5s for permission
                .build();

        return RateLimiterRegistry.of(config);
    }
}
