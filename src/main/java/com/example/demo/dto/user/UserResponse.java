package com.example.demo.dto.user;

import com.example.demo.enums.SystemRole;

import java.time.LocalDateTime;

public record UserResponse(

        Long id,

        String username,

        String email,

        SystemRole systemRole,

        LocalDateTime createdAt

) {
}