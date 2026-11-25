package com.example.demo.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank(message = "требуется username")
        String username,

        @NotBlank(message = "требуется password")
        String password

) {
}
