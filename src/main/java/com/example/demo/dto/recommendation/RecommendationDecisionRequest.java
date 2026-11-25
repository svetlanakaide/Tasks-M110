package com.example.demo.dto.recommendation;

import jakarta.validation.constraints.NotNull;

public record RecommendationDecisionRequest(

        @NotNull(message = "требуется решение (accepted)")
        Boolean accepted

) {
}