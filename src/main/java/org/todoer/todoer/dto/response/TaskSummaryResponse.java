package org.todoer.todoer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskSummaryResponse {
    private Long id;
    private String title;
    private String status;
    private Integer priority;
    private ZonedDateTime dueDate;
    private UserSummaryResponse assignedTo;
}
