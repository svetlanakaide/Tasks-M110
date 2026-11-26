package com.example.demo.dto.auth;

public record AuthResponse(

        String accessToken,

        String refreshToken,

        String tokenType,

        long expiresIn

) {
}
