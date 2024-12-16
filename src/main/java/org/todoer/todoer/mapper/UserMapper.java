package org.todoer.todoer.mapper;

import org.mapstruct.*;
import org.todoer.todoer.dto.request.UserRegistrationRequest;
import org.todoer.todoer.dto.request.UserUpdateRequest;
import org.todoer.todoer.dto.response.UserResponse;
import org.todoer.todoer.dto.response.UserSummaryResponse;
import org.todoer.todoer.entities.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hashedPassword", ignore = true)  // ignore passwordHash during creation
    @Mapping(target = "projects", ignore = true)
    @Mapping(target = "assignedTasks", ignore = true)
    @Mapping(target = "createdTasks", ignore = true)
    @Mapping(target = "auditMetadata.createdAt", ignore = true)
    @Mapping(target = "auditMetadata.updatedAt", ignore = true)
    User toEntity(UserRegistrationRequest request);

    UserResponse toResponse(User user);

    UserSummaryResponse toSummaryResponse(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget User user, UserUpdateRequest request);
}
