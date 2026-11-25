package com.example.demo.dto.project;

import jakarta.validation.constraints.Size;

public record UpdateProjectRequest(

        @Size(min = 1, max = 200, message = "название проекта от 1 до 200 символов")
        String name,

        String description

) {
}