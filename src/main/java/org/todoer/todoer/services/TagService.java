package org.todoer.todoer.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.todoer.todoer.dto.request.TagCreateRequest;
import org.todoer.todoer.dto.request.TagUpdateRequest;
import org.todoer.todoer.dto.response.TagResponse;
import org.todoer.todoer.entities.Project;
import org.todoer.todoer.entities.Tag;
import org.todoer.todoer.exceptions.BusinessException;
import org.todoer.todoer.exceptions.ResourceNotFoundException;
import org.todoer.todoer.mapper.TagMapper;
import org.todoer.todoer.repositories.ProjectRepository;
import org.todoer.todoer.repositories.TagRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final ProjectRepository projectRepository;
    private final TagMapper tagMapper;

    public TagResponse createTag(TagCreateRequest request) {
        if (tagRepository.existsByNameAndProjectId(request.getName(), request.getProjectId())) {
            throw new BusinessException("Tag already exists in this project");
        }

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        Tag tag = tagMapper.toEntity(request);
        tag.setProject(project);

        return tagMapper.toResponse(tagRepository.save(tag));
    }

    public List<TagResponse> getProjectTags(Long projectId) {
        return tagRepository.findByProjectId(projectId).stream()
                .map(tagMapper::toResponse)
                .collect(Collectors.toList());
    }

    public TagResponse updateTag(Long tagId, TagUpdateRequest request) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found"));

        tagMapper.updateEntity(tag, request);

        return tagMapper.toResponse(tagRepository.save(tag));
    }
}
