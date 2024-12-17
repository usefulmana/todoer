package org.todoer.todoer.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.todoer.todoer.dto.request.TaskCreateRequest;
import org.todoer.todoer.dto.request.TaskUpdateRequest;
import org.todoer.todoer.dto.response.TaskResponse;
import org.todoer.todoer.entities.Project;
import org.todoer.todoer.entities.Task;
import org.todoer.todoer.entities.TaskStatus;
import org.todoer.todoer.entities.User;
import org.todoer.todoer.exceptions.ResourceNotFoundException;
import org.todoer.todoer.mapper.TaskMapper;
import org.todoer.todoer.repositories.ProjectRepository;
import org.todoer.todoer.repositories.TaskRepository;
import org.todoer.todoer.repositories.TaskSpecifications;
import org.todoer.todoer.repositories.UserRepository;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;
    private final FirebaseNotificationService firebaseNotificationService;

    public TaskResponse createTask(Long userId, TaskCreateRequest request) {
        Project project = projectRepository.findById(request.getProjectId()).orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        User creator = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        User assignee = null;
        if (request.getAssignedToId() != null) {
            assignee = userRepository.findById(request.getAssignedToId()).orElseThrow(() -> new ResourceNotFoundException("Assignee not found"));
        }

        Task task = taskMapper.toEntity(request);
        task.setProject(project);
        task.setCreatedBy(creator);
        task.setAssignedTo(assignee);

        Task savedTask = taskRepository.save(task);
        TaskResponse response = taskMapper.toResponse(savedTask);

        firebaseNotificationService.sendTaskNotification(
                "project-" + task.getProject().getId(), // topic format: "project-{projectId}"
                response
        ).thenAccept(messageId -> {
            log.info("Notification sent for task: {}, messageId: {}", task.getId(), messageId);
        }).exceptionally(error -> {
            log.error("Failed to send notification for task: {}", task.getId(), error);
            return null;
        });

        return response;
    }

    public Page<TaskResponse> getProjectTasks(Long projectId, String status, Pageable pageable) {
        Specification<Task> spec = Specification.where(TaskSpecifications.inProject(projectId));

        if (status != null) {
            spec = spec.and(TaskSpecifications.hasStatus(TaskStatus.valueOf(status)));
        }

        return taskRepository.findAll(spec, pageable).map(taskMapper::toResponse);
    }

    public TaskResponse updateTask(Long taskId, TaskUpdateRequest request) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        User previousAssignee = task.getAssignedTo();

        taskMapper.updateEntity(task, request);

        if (request.getAssignedToId() != null && !request.getAssignedToId().equals(previousAssignee != null ? previousAssignee.getId() : null)) {
            User newAssignee = userRepository.findById(request.getAssignedToId()).orElseThrow(() -> new ResourceNotFoundException("Assignee not found"));
            task.setAssignedTo(newAssignee);
        }

        return taskMapper.toResponse(taskRepository.save(task));
    }

    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    public Page<TaskResponse> findTasks(Specification<Task> spec, Pageable pageable) {
        return taskRepository.findAll(spec, pageable)
                .map(taskMapper::toResponse);
    }
}
