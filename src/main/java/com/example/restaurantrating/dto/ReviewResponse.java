package com.example.restaurantrating.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ReviewResponse(
        @Schema(description = "ID посетителя")
        Long visitorId,
        @Schema(description = "ID ресторана")
        Long restaurantId,
        int rating,
        String comment
) {}