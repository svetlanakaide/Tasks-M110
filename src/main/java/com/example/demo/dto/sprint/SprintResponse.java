package com.example.demo.dto.sprint;

import com.example.demo.enums.SprintStatus;

import java.time.LocalDate;

public record SprintResponse(

        Long id,

        Long projectId,

        String name,

        LocalDate startDate,

        LocalDate endDate,

        SprintStatus status

) {
}