package org.todoer.todoer.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.todoer.todoer.dto.request.TaskCreateRequest;
import org.todoer.todoer.dto.request.TaskUpdateRequest;
import org.todoer.todoer.dto.response.TaskResponse;
import org.todoer.todoer.entities.Task;
import org.todoer.todoer.entities.TaskStatus;
import org.todoer.todoer.repositories.TaskSpecifications;
import org.todoer.todoer.services.TaskService;
import org.todoer.todoer.util.Constants;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse createTask(
            @RequestHeader(Constants.X_PROJECT_ID) Long projectId,
            @RequestHeader(Constants.X_USER_ID) Long userId,
            @Valid @RequestBody TaskCreateRequest request) {
        request.setProjectId(projectId);
        return taskService.createTask(userId, request);
    }

    @GetMapping
    public Page<TaskResponse> getProjectTasks(
            @RequestHeader(Constants.X_PROJECT_ID) Long projectId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer priority,
            @RequestParam(required = false) Long assigneeId,
            @PageableDefault(sort = "dueDate", direction = Sort.Direction.ASC) Pageable pageable) {

        Specification<Task> spec = Specification
                .where(TaskSpecifications.inProject(projectId))
                .and(TaskSpecifications.hasStatus(status != null ? TaskStatus.valueOf(status) : null))
                .and(TaskSpecifications.hasPriority(priority))
                .and(TaskSpecifications.hasAssignee(assigneeId));

        return taskService.findTasks(spec, pageable);
    }

    @PutMapping("/{taskId}")
    public TaskResponse updateTask(
            @PathVariable Long taskId,
            @Valid @RequestBody TaskUpdateRequest request) {
        return taskService.updateTask(taskId, request);
    }

    @DeleteMapping("/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
    }
}
