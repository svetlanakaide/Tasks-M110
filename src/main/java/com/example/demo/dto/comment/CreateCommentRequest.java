package com.example.demo.dto.comment;

import jakarta.validation.constraints.NotBlank;

public record CreateCommentRequest(

        @NotBlank(message = "комментарий не может быть пустым")
        String text

) {
}