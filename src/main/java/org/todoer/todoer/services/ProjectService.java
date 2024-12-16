package org.todoer.todoer.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.todoer.todoer.dto.request.ProjectCreateRequest;
import org.todoer.todoer.dto.request.ProjectUpdateRequest;
import org.todoer.todoer.dto.response.ProjectResponse;
import org.todoer.todoer.entities.Project;
import org.todoer.todoer.entities.User;
import org.todoer.todoer.exceptions.BusinessException;
import org.todoer.todoer.exceptions.ResourceNotFoundException;
import org.todoer.todoer.mapper.ProjectMapper;
import org.todoer.todoer.repositories.ProjectRepository;
import org.todoer.todoer.repositories.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;

    public ProjectResponse createProject(Long userId, ProjectCreateRequest request) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Project project = projectMapper.toEntity(request);
        project.setOwner(owner);

        return projectMapper.toResponse(projectRepository.save(project));
    }

    public Page<ProjectResponse> getUserProjects(Long userId, Pageable pageable) {
        return projectRepository.findByOwnerId(userId, pageable)
                .map(projectMapper::toResponse);
    }

    public ProjectResponse updateProject(Long projectId, ProjectUpdateRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        projectMapper.updateEntity(project, request);

        return projectMapper.toResponse(projectRepository.save(project));
    }

    public void deleteProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        if (!project.isDeletable()) {
            throw new BusinessException("This project cannot be deleted");
        }

        projectRepository.delete(project);
    }
}
