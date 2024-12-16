package org.todoer.todoer.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskUpdateRequest {
    @Size(min = 1, max = 200, message = "Task title must be between 1 and 200 characters")
    private String title;

    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;

    @Pattern(regexp = "^(TODO|IN_PROGRESS|DONE)$", message = "Status must be either TODO, IN_PROGRESS, or DONE")
    private String status;

    @Min(value = 0, message = "Priority must be at least 0")
    @Max(value = 5, message = "Priority must not exceed 5")
    private Integer priority;

    @Future(message = "Due date must be in the future")
    private ZonedDateTime dueDate;

    @Positive(message = "Assigned user ID must be positive")
    private Long assignedToId;

    @Builder.Default
    private Set<@Positive(message = "Tag ID must be positive") Long> tagIds = new HashSet<>();
}