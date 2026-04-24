package com.example.restaurantrating.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ReviewRequest(
        @Schema(description = "ID посетителя")
        Long visitorId,
        @Schema(description = "ID ресторана")
        Long restaurantId,
        @Schema(description = "Оценка от 1 до 5")
        int rating,
        @Schema(description = "Текст отзыва")
        String comment
) {}