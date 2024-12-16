package org.todoer.todoer.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private int status;
    private String message;
    private String path;
    private LocalDateTime timestamp;
    private String traceId;
    private List<ValidationError> errors;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidationError {
        private String field;
        private String message;
        private String code;
        private Object rejectedValue;
    }

    public void addValidationError(String field, String message) {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        errors.add(ValidationError.builder()
                .field(field)
                .message(message)
                .build());
    }

    public void addValidationError(String field, String message, String code, Object rejectedValue) {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        errors.add(ValidationError.builder()
                .field(field)
                .message(message)
                .code(code)
                .rejectedValue(rejectedValue)
                .build());
    }
}
