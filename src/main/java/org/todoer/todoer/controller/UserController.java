package org.todoer.todoer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.todoer.todoer.dto.request.UserRegistrationRequest;
import org.todoer.todoer.dto.response.ProjectResponse;
import org.todoer.todoer.dto.response.TaskResponse;
import org.todoer.todoer.dto.response.UserResponse;
import org.todoer.todoer.services.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@Valid @RequestBody UserRegistrationRequest request) {
        return userService.createUser(request);
    }

    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/{id}/projects")
    public Page<ProjectResponse> getUserProjects(
            @PathVariable Long id,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return userService.getUserProjects(id, pageable);
    }

    @GetMapping("/{id}/tasks")
    public Page<TaskResponse> getUserTasks(
            @PathVariable Long id,
            @PageableDefault(sort = "dueDate", direction = Sort.Direction.ASC) Pageable pageable) {
        return userService.getUserTasks(id, pageable);
    }
}
