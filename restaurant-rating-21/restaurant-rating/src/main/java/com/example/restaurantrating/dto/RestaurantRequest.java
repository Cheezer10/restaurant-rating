package com.example.restaurantrating.dto;

import com.example.restaurantrating.enums.CuisineType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record RestaurantRequest(
        @Schema(description = "Название ресторана")
        String name,
        @Schema(description = "Описание")
        String description,
        @Schema(description = "Тип кухни")
        CuisineType cuisineType,
        @Schema(description = "Средний чек")
        BigDecimal averageBill
) {}