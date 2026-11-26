package com.example.demo.dto.comment;

import com.example.demo.dto.user.UserShortResponse;

import java.time.LocalDateTime;

public record CommentResponse(

        Long id,

        Long taskId,

        UserShortResponse author,

        String text,

        LocalDateTime createdAt,

        LocalDateTime updatedAt

) {
}