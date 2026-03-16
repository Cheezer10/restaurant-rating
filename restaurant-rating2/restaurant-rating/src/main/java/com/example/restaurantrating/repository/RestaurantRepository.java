package com.example.restaurantrating.repository;

import com.example.restaurantrating.entity.Restaurant;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class RestaurantRepository {
    private final List<Restaurant> restaurants = new ArrayList<>();
    private long nextId = 1;

    public Restaurant save(Restaurant restaurant) {
        if (restaurant.getId() == null) {
            restaurant.setId(nextId++);
        } else {
            restaurants.removeIf(r -> Objects.equals(r.getId(), restaurant.getId()));
        }
        restaurants.add(restaurant);
        return restaurant;
    }

    public void remove(Long id) {
        restaurants.removeIf(r -> Objects.equals(r.getId(), id));
    }

    public List<Restaurant> findAll() {
        return new ArrayList<>(restaurants);
    }

    public Optional<Restaurant> findById(Long id) {
        return restaurants.stream()
                .filter(r -> Objects.equals(r.getId(), id))
                .findFirst();
    }
}