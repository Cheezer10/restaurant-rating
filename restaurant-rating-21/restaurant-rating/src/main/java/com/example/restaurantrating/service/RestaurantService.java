package com.example.restaurantrating.service;

import com.example.restaurantrating.dto.RestaurantRequest;
import com.example.restaurantrating.dto.RestaurantResponse;
import com.example.restaurantrating.entity.Restaurant;
import com.example.restaurantrating.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    private RestaurantResponse toResponse(Restaurant restaurant) {
        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getDescription(),
                restaurant.getCuisineType(),
                restaurant.getAverageBill(),
                restaurant.getAverageRating() != null ? restaurant.getAverageRating() : BigDecimal.ZERO
        );
    }

    private Restaurant toEntity(RestaurantRequest request) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.name());
        restaurant.setDescription(request.description());
        restaurant.setCuisineType(request.cuisineType());
        restaurant.setAverageBill(request.averageBill());
        restaurant.setAverageRating(BigDecimal.ZERO); // начальное значение
        return restaurant;
    }

    public RestaurantResponse save(RestaurantRequest request) {
        Restaurant restaurant = toEntity(request);
        Restaurant saved = restaurantRepository.save(restaurant);
        return toResponse(saved);
    }

    public RestaurantResponse update(Long id, RestaurantRequest request) {
        Restaurant existing = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        existing.setName(request.name());
        existing.setDescription(request.description());
        existing.setCuisineType(request.cuisineType());
        existing.setAverageBill(request.averageBill());
        Restaurant updated = restaurantRepository.save(existing);
        return toResponse(updated);
    }

    public void remove(Long id) {
        restaurantRepository.remove(id);
    }

    public List<RestaurantResponse> findAll() {
        return restaurantRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public RestaurantResponse findById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        return toResponse(restaurant);
    }
}