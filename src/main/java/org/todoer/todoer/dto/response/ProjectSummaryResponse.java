package org.todoer.todoer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectSummaryResponse {
    private Long id;
    private String name;
    private UserSummaryResponse owner;
}
