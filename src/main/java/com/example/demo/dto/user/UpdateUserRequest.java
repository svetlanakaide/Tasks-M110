package com.example.demo.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(

        @Size(min = 2, max = 100, message = "username должен быть от 2 до 100 символов")
        String username,

        @Email(message = "email неимейлиться по формату")
        @Size(max = 255, message = "email не длиннее 255 символов")
        String email

) {
}