package org.todoer.todoer.mapper;

import org.mapstruct.*;
import org.todoer.todoer.dto.request.TagCreateRequest;
import org.todoer.todoer.dto.request.TagUpdateRequest;
import org.todoer.todoer.dto.response.TagResponse;
import org.todoer.todoer.entities.Tag;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "auditMetadata.createdAt", ignore = true)
    Tag toEntity(TagCreateRequest request);

    TagResponse toResponse(Tag tag);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget Tag tag, TagUpdateRequest request);
}
