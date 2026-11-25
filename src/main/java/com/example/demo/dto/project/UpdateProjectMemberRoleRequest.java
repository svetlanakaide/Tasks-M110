package com.example.demo.dto.project;

import com.example.demo.enums.ProjectRole;
import jakarta.validation.constraints.NotNull;

public record UpdateProjectMemberRoleRequest(

        @NotNull(message = "требуется роль в проекте")
        ProjectRole role

) {
}