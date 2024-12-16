package org.todoer.todoer.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.todoer.todoer.entities.NotificationStatus;
import org.todoer.todoer.repositories.NotificationFailureRepository;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationHealthCheckJob {

    private final NotificationFailureRepository notificationFailureRepository;

    @Scheduled(cron = "0 0 * * * *")
    public void checkFailedNotifications() {
        long failedCount = notificationFailureRepository.countByStatus(NotificationStatus.FAILED);
        if (failedCount > 100) { // arbitrary threshold
            log.warn("High number of failed notifications: {}", failedCount);
        }

        // Clean up old sent notifications
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        notificationFailureRepository.deleteOldSentNotifications(oneWeekAgo);
    }
}
