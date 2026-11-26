package com.example.demo.dto.recommendation;

import com.example.demo.dto.user.UserShortResponse;

public record RecommendationResponse(

        Long id,

        Long taskId,

        UserShortResponse suggestedUser,

        String reason,

        Boolean accepted

) {
}