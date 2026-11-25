package com.example.demo.dto.task;

import com.example.demo.enums.TaskStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record UpdateTaskRequest(

        @Size(min = 1, max = 255, message = "название задачи от 1 до 255 символов")
        String name,

        String description,

        Long sprintId,

        Long parentId,

        TaskStatus status,

        @Min(value = 1, message = "приоритет от 1 до 5")
        @Max(value = 5, message = "приоритет от 1 до 5")
        Integer priority,

        @Min(value = 0, message = "оценка не может быть отрицательной")
        Integer estimate,

        @Size(max = 255, message = "требование не длиннее 255 символов")
        String requirement

) {
}