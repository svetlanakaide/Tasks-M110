package com.example.demo.dto.sprint;

import com.example.demo.enums.SprintStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateSprintRequest(

        @Size(min = 1, max = 200, message = "название спринта от 1 до 200 символов")
        String name,

        LocalDate startDate,

        LocalDate endDate,

        SprintStatus status

) {
    @JsonIgnore
    @AssertTrue(message = "дата окончания не может быть раньше даты начала")
    public boolean isDateRangeValid() {
        return startDate == null || endDate == null || !endDate.isBefore(startDate);
    }
}