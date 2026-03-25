package com.example.restaurantrating.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserRequest(
        @Schema(description = "Имя посетителя (может быть null для анонимного)", nullable = true)
        String name,
        @Schema(description = "Возраст", example = "30")
        int age,
        @Schema(description = "Пол", example = "М")
        String gender
) {}