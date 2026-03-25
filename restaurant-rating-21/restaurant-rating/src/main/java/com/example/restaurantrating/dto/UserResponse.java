package com.example.restaurantrating.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserResponse(
        @Schema(description = "ID посетителя")
        Long id,
        String name,
        int age,
        String gender
) {}