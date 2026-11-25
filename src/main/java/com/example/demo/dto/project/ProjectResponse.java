package com.example.demo.dto.project;

import com.example.demo.dto.user.UserShortResponse;

import java.time.LocalDateTime;

public record ProjectResponse(

        Long id,

        String name,

        String description,

        UserShortResponse owner,

        LocalDateTime createdAt

) {
}