package org.todoer.todoer.mapper;

import org.mapstruct.*;
import org.todoer.todoer.dto.request.TaskCreateRequest;
import org.todoer.todoer.dto.request.TaskUpdateRequest;
import org.todoer.todoer.dto.response.TaskResponse;
import org.todoer.todoer.dto.response.TaskSummaryResponse;
import org.todoer.todoer.entities.Task;
import org.todoer.todoer.entities.TaskStatus;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "assignedTo", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "auditMetadata.createdAt", ignore = true)
    @Mapping(target = "auditMetadata.updatedAt", ignore = true)
    Task toEntity(TaskCreateRequest request);

    TaskResponse toResponse(Task task);

    TaskSummaryResponse toSummaryResponse(Task task);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget Task task, TaskUpdateRequest request);

    default TaskStatus mapStatus(String status) {
        if (status == null) return null;
        return TaskStatus.valueOf(status);
    }

    default String mapTaskStatus(TaskStatus status) {
        if (status == null) return null;
        return status.name();
    }
}
