package com.example.demo.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank(message = "требуется username")
        @Size(min = 2, max = 100)
        String username,

        @NotBlank(message = "требуется email")
        @Email(message = "email неимейлиться по формату")
        @Size(max = 255)
        String email,

        @NotBlank(message = "требуется password")
        @Size(min = 8, max = 72)
        String password

) {
}
