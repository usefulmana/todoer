package org.todoer.todoer.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    @Email(message = "Please provide a valid email address")
    @Size(max = 255, message = "Email must be less than 255 characters")
    private String email;

    @Pattern(regexp = "^[a-zA-Z0-9_-]{3,50}$", message = "Username must be 3-50 characters long and can only contain letters, numbers, underscores, and hyphens")
    private String username;

    @NotBlank(message = "Current password is required")
    private String currentPassword;

    @Size(min = 8, max = 100, message = "New password must be between 8 and 100 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
            message = "New password must contain at least one digit, one lowercase, one uppercase, and one special character")
    private String newPassword;
}
