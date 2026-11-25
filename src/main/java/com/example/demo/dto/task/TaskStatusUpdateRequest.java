package com.example.demo.dto.task;

import com.example.demo.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;

public record TaskStatusUpdateRequest(

        @NotNull(message = "требуется статус")
        TaskStatus status

) {
}