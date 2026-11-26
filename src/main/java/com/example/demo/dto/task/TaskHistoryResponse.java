package com.example.demo.dto.task;

import com.example.demo.dto.user.UserShortResponse;

import java.time.LocalDateTime;

public record TaskHistoryResponse(

        Long id,

        Long taskId,

        UserShortResponse user,

        String fieldChanged,

        String oldValue,

        String newValue,

        LocalDateTime updatedAt

) {
}