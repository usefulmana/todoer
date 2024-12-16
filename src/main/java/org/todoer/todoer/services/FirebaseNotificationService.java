package org.todoer.todoer.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.todoer.todoer.dto.response.TaskResponse;
import org.todoer.todoer.entities.NotificationFailure;
import org.todoer.todoer.entities.NotificationStatus;
import org.todoer.todoer.repositories.NotificationFailureRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class FirebaseNotificationService {
    private final FirebaseMessaging firebaseMessaging;
    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final RateLimiterRegistry rateLimiterRegistry;
    private final NotificationFailureRepository notificationFailureRepository;

    @PostConstruct
    public void init() {
        // Register event listeners for circuit breaker state transitions
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("firebase");
        circuitBreaker.getEventPublisher()
                .onStateTransition(event ->
                        log.warn("Circuit breaker state changed from {} to {}",
                                event.getStateTransition().getFromState(),
                                event.getStateTransition().getToState()));
    }

    @Async
    public CompletableFuture<String> sendTaskNotification(String topic, TaskResponse task) {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("firebase");
        RateLimiter rateLimiter = rateLimiterRegistry.rateLimiter("firebase");

        return CompletableFuture.supplyAsync(() -> {
            Message message = Message.builder()
                    .setTopic(topic)
                    .putData("type", "TASK_CREATED")
                    .putData("taskId", task.getId().toString())
                    .putData("title", task.getTitle())
                    .putData("projectId", task.getProject().getId().toString())
                    .build();

            return CircuitBreaker.decorateSupplier(circuitBreaker, () ->
                    RateLimiter.decorateSupplier(rateLimiter, () -> {
                        try {
                            return firebaseMessaging.send(message);
                        } catch (FirebaseMessagingException e) {
                            handleFailure(topic, task, e);
                            throw new CompletionException(e);
                        }
                    }).get()
            ).get();
        }).exceptionally(throwable -> {
            log.error("Failed to send notification", throwable);
            return null; // or return a failure status
        });
    }

    private void handleFailure(String topic, TaskResponse task, Exception e) {
        // Store failed notification for retry
        NotificationFailure failure = NotificationFailure.builder()
                .topic(topic)
                .taskId(task.getId())
                .payload(convertTaskToPayload(task))
                .failureReason(e.getMessage())
                .retryCount(0)
                .status(NotificationStatus.FAILED)
                .createdAt(LocalDateTime.now())
                .build();

        notificationFailureRepository.save(failure);
    }

    @Scheduled(fixedDelay = 300000) // Every 5 minutes
    public void retryFailedNotifications() {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("firebase");

        // Only retry if circuit is closed
        if (circuitBreaker.getState() == CircuitBreaker.State.CLOSED) {
            List<NotificationFailure> failures = notificationFailureRepository
                    .findByStatusAndRetryCountLessThan(NotificationStatus.FAILED, 3);

            for (NotificationFailure failure : failures) {
                try {
                    firebaseMessaging.send(reconstructMessage(failure));
                    failure.setStatus(NotificationStatus.SENT);
                    notificationFailureRepository.save(failure);
                } catch (FirebaseMessagingException e) {
                    failure.setRetryCount(failure.getRetryCount() + 1);
                    failure.setLastAttempt(LocalDateTime.now());
                    notificationFailureRepository.save(failure);
                }
            }
        }
    }

    @Scheduled(fixedRate = 60000) // Every minute
    public void logMetrics() {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("firebase");
        RateLimiter rateLimiter = rateLimiterRegistry.rateLimiter("firebase");

        CircuitBreaker.Metrics circuitMetrics = circuitBreaker.getMetrics();
        RateLimiter.Metrics rateLimiterMetrics = rateLimiter.getMetrics();

        log.info("Firebase Service Metrics - " +
                        "Circuit State: {}, Failure Rate: {}%, " +
                        "Rate Limiter Available Permissions: {}, " +
                        "Waiting Threads: {}",
                circuitBreaker.getState(),
                circuitMetrics.getFailureRate(),
                rateLimiterMetrics.getAvailablePermissions(),
                rateLimiterMetrics.getNumberOfWaitingThreads());
    }

    private String convertTaskToPayload(TaskResponse task) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> payload = new HashMap<>();
            payload.put("type", "TASK_CREATED");
            payload.put("taskId", task.getId().toString());
            payload.put("title", task.getTitle());
            payload.put("projectId", task.getProject().getId().toString());
            payload.put("status", task.getStatus());
            payload.put("priority", String.valueOf(task.getPriority()));
            if (task.getDueDate() != null) {
                payload.put("dueDate", task.getDueDate().toString());
            }
            return mapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            log.error("Error converting task to payload", e);
            throw new RuntimeException("Failed to serialize task payload", e);
        }
    }

    private Message reconstructMessage(NotificationFailure failure) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> payload = mapper.readValue(failure.getPayload(),
                    new TypeReference<Map<String, String>>() {});

            return Message.builder()
                    .setTopic(failure.getTopic())
                    .putAllData(payload)
                    .build();
        } catch (JsonProcessingException e) {
            log.error("Error reconstructing message from failure", e);
            throw new RuntimeException("Failed to reconstruct message", e);
        }
    }
}
