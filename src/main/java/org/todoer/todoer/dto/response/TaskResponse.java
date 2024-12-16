package org.todoer.todoer.dto.response;

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
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private String status;
    private Integer priority;
    private ZonedDateTime dueDate;
    private ProjectSummaryResponse project;
    private UserSummaryResponse assignedTo;
    private UserSummaryResponse createdBy;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    @Builder.Default
    private Set<TagResponse> tags = new HashSet<>();
}
