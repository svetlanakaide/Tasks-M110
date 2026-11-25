package com.example.demo.dto.project;

import com.example.demo.enums.ProjectRole;
import jakarta.validation.constraints.NotNull;

public record AddProjectMemberRequest(

        @NotNull(message = "требуется userId")
        Long userId,

        @NotNull(message = "требуется роль в проекте")
        ProjectRole role

) {
}