package org.todoer.todoer.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import org.todoer.todoer.entities.DeviceType;

@Data
public class DeviceTokenRequest {
    @NotBlank
    private String token;

    @NotBlank
    private DeviceType deviceType;
}
