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
public class ProjectResponse {
    private Long id;
    private String name;
    private String description;
    private UserSummaryResponse owner;
    private boolean isDeletable;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    @Builder.Default
    private Set<TaskSummaryResponse> tasks = new HashSet<>();
    @Builder.Default
    private Set<TagResponse> tags = new HashSet<>();
}
