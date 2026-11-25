package com.example.demo.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(

        @NotBlank(message = "требуется текущий пароль")
        String oldPassword,

        @NotBlank(message = "требуется новый пароль")
        @Size(min = 8, max = 72, message = "новый пароль должен быть от 8 до 72 символов")
        String newPassword

) {
}