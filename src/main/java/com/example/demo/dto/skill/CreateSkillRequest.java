package com.example.demo.dto.skill;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateSkillRequest(

        @NotBlank(message = "требуется название навыка")
        @Size(max = 100, message = "название навыка не длиннее 100 символов")
        String name

) {
}