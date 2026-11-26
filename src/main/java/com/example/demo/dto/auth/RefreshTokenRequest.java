package com.example.demo.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(

        @NotBlank(message = "требуется refresh token")
        String refreshToken

) {
}
