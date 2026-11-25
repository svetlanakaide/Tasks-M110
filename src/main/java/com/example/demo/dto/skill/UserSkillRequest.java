package com.example.demo.dto.skill;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UserSkillRequest(

        @NotNull(message = "требуется skillId")
        Long skillId,

        @NotNull(message = "требуется уровень")
        @Min(value = 1, message = "уровень от 1 до 5")
        @Max(value = 5, message = "уровень от 1 до 5")
        Integer level

) {
}