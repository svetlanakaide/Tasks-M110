package com.example.demo.dto.task;

import com.example.demo.dto.user.UserShortResponse;
import com.example.demo.enums.TaskStatus;

public record TaskShortResponse(

        Long id,

        Long parentId,

        TaskStatus status,

        String name,

        Integer priority,

        UserShortResponse assignee,

        Integer estimate

) {
}