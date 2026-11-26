package com.example.demo.dto.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateProjectRequest(

        @NotBlank(message = "требуется название проекта")
        @Size(max = 200, message = "название проекта не длиннее 200 символов")
        String name,

        String description

) {
}