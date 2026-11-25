package com.example.demo.dto.task;

import com.example.demo.dto.user.UserShortResponse;
import com.example.demo.enums.TaskStatus;

import java.time.LocalDateTime;

public record TaskResponse(

        Long id,

        Long projectId,

        Long sprintId,

        Long parentId,

        TaskStatus status,

        String name,

        String description,

        Integer priority,

        UserShortResponse assignee,

        UserShortResponse reporter,

        Integer estimate,

        String requirement,

        LocalDateTime createdAt,

        LocalDateTime updatedAt,

        LocalDateTime completedAt

) {
}