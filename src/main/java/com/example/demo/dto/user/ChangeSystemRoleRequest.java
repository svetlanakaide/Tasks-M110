package com.example.demo.dto.user;

import com.example.demo.enums.SystemRole;
import jakarta.validation.constraints.NotNull;

public record ChangeSystemRoleRequest(

        @NotNull(message = "требуется systemRole")
        SystemRole systemRole

) {
}