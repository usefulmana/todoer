package org.todoer.todoer.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.todoer.todoer.dto.request.UserRegistrationRequest;
import org.todoer.todoer.dto.request.UserUpdateRequest;
import org.todoer.todoer.dto.response.ProjectResponse;
import org.todoer.todoer.dto.response.TaskResponse;
import org.todoer.todoer.dto.response.UserResponse;
import org.todoer.todoer.entities.User;
import org.todoer.todoer.exceptions.BusinessException;
import org.todoer.todoer.exceptions.ResourceNotFoundException;
import org.todoer.todoer.mapper.ProjectMapper;
import org.todoer.todoer.mapper.TaskMapper;
import org.todoer.todoer.mapper.UserMapper;
import org.todoer.todoer.repositories.ProjectRepository;
import org.todoer.todoer.repositories.TaskRepository;
import org.todoer.todoer.repositories.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;

    public UserResponse createUser(UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email already exists");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("Username already exists");
        }

        User user = userMapper.toEntity(request);
        return userMapper.toResponse(userRepository.save(user));
    }

    public UserResponse updateUser(Long userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userMapper.updateEntity(user, request);
        return userMapper.toResponse(userRepository.save(user));
    }

    public UserResponse getUserById(Long userId) {
        return userRepository.findById(userId)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toResponse);
    }

    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(userId);
    }

    public Page<ProjectResponse> getUserProjects(Long userId, Pageable pageable) {
        return projectRepository.findByOwnerId(userId, pageable)
                .map(projectMapper::toResponse);
    }

    public Page<TaskResponse> getUserTasks(Long userId, Pageable pageable) {
        return taskRepository.findByAssignedToId(userId, pageable)
                .map(taskMapper::toResponse);
    }
}
