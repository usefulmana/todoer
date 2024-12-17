package org.todoer.todoer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.todoer.todoer.dto.request.ProjectCreateRequest;
import org.todoer.todoer.dto.request.ProjectUpdateRequest;
import org.todoer.todoer.dto.response.ProjectResponse;
import org.todoer.todoer.services.ProjectService;
import org.todoer.todoer.util.Constants;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectResponse createProject(
            @RequestHeader(Constants.X_USER_ID) Long userId,
            @Valid @RequestBody ProjectCreateRequest request) {
        return projectService.createProject(userId, request);
    }

    @GetMapping("/{id}")
    public ProjectResponse getProject(@PathVariable Long id) {
        return projectService.getProjectById(id);
    }

    @PutMapping("/{id}")
    public ProjectResponse updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectUpdateRequest request) {
        return projectService.updateProject(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
    }
}
