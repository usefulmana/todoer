package org.todoer.todoer.mapper;

import org.mapstruct.*;
import org.todoer.todoer.dto.request.ProjectCreateRequest;
import org.todoer.todoer.dto.request.ProjectUpdateRequest;
import org.todoer.todoer.dto.response.ProjectResponse;
import org.todoer.todoer.dto.response.ProjectSummaryResponse;
import org.todoer.todoer.entities.Project;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProjectMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "auditMetadata.createdAt", ignore = true)
    @Mapping(target = "auditMetadata.updatedAt", ignore = true)
    @Mapping(target = "deletable", constant = "true")
    Project toEntity(ProjectCreateRequest request);

    ProjectResponse toResponse(Project project);

    ProjectSummaryResponse toSummaryResponse(Project project);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget Project project, ProjectUpdateRequest request);
}
