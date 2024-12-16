package org.todoer.todoer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.todoer.todoer.entities.NotificationFailure;
import org.todoer.todoer.entities.NotificationStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationFailureRepository extends JpaRepository<NotificationFailure, Long> {

    // Find failures for retry
    List<NotificationFailure> findByStatusAndRetryCountLessThan(
            NotificationStatus status,
            int maxRetries
    );

    // Find failures for a specific task
    List<NotificationFailure> findByTaskId(Long taskId);

    // Find failures within a time range
    @Query("SELECT nf FROM NotificationFailure nf " +
            "WHERE nf.createdAt BETWEEN :startTime AND :endTime")
    List<NotificationFailure> findFailuresInTimeRange(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    // Find old sent notifications for cleanup
    @Query("SELECT nf FROM NotificationFailure nf " +
            "WHERE nf.status = 'SENT' " +
            "AND nf.createdAt < :beforeTime")
    List<NotificationFailure> findOldSentNotifications(
            @Param("beforeTime") LocalDateTime beforeTime
    );

    // Count failures by status
    long countByStatus(NotificationStatus status);

    // Count failures for a specific task
    long countByTaskIdAndStatus(Long taskId, NotificationStatus status);

    // Find failures that need immediate retry (haven't been retried recently)
    @Query("SELECT nf FROM NotificationFailure nf " +
            "WHERE nf.status = 'FAILED' " +
            "AND nf.retryCount < :maxRetries " +
            "AND (nf.lastAttempt IS NULL OR nf.lastAttempt < :cutoffTime)")
    List<NotificationFailure> findFailuresForImmediateRetry(
            @Param("maxRetries") int maxRetries,
            @Param("cutoffTime") LocalDateTime cutoffTime
    );

    // Delete old sent notifications
    @Query("DELETE FROM NotificationFailure nf " +
            "WHERE nf.status = 'SENT' " +
            "AND nf.createdAt < :beforeTime")
    void deleteOldSentNotifications(@Param("beforeTime") LocalDateTime beforeTime);
}
