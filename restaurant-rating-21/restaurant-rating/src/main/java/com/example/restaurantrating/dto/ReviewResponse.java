package com.example.restaurantrating.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ReviewResponse(
        @Schema(description = "ID отзыва")
        Long id,
        Long visitorId,
        Long restaurantId,
        int rating,
        String comment
) {}