package org.todoer.todoer.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_failures")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationFailure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String topic;
    private Long taskId;

    @Column(columnDefinition = "TEXT")
    private String payload;

    private String failureReason;
    private int retryCount;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime lastAttempt;
}