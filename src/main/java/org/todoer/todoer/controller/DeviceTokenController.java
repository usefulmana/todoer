package org.todoer.todoer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.todoer.todoer.dto.request.DeviceTokenRequest;
import org.todoer.todoer.entities.DeviceToken;
import org.todoer.todoer.entities.User;
import org.todoer.todoer.exceptions.ResourceNotFoundException;
import org.todoer.todoer.repositories.DeviceTokenRepository;
import org.todoer.todoer.repositories.UserRepository;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/device-tokens")
public class DeviceTokenController {
    private final DeviceTokenRepository deviceTokenRepository;
    private final UserRepository userRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registerToken(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody DeviceTokenRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        DeviceToken deviceToken = new DeviceToken();
        deviceToken.setToken(request.getToken());
        deviceToken.setUser(user);
        deviceToken.setDeviceType(request.getDeviceType());

        deviceTokenRepository.save(deviceToken);
    }

    @DeleteMapping("/{token}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unregisterToken(@PathVariable String token) {
        deviceTokenRepository.deleteByToken(token);
    }
}
