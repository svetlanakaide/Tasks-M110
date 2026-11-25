package com.example.demo.dto.sprint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateSprintRequest(

        @NotBlank(message = "требуется название спринта")
        @Size(max = 200, message = "название спринта не длиннее 200 символов")
        String name,

        LocalDate startDate,

        LocalDate endDate

) {
    @JsonIgnore
    @AssertTrue(message = "дата окончания не может быть раньше даты начала")
    public boolean isDateRangeValid() {
        return startDate == null || endDate == null || !endDate.isBefore(startDate);
    }
}