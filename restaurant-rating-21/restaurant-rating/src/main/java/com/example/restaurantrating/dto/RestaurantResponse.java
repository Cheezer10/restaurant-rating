package com.example.restaurantrating.dto;

import com.example.restaurantrating.enums.CuisineType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record RestaurantResponse(
        Long id,
        String name,
        String description,
        CuisineType cuisineType,
        BigDecimal averageBill,
        BigDecimal averageRating
) {}