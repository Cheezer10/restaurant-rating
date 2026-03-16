package com.example.restaurantrating.repository;

import com.example.restaurantrating.entity.Rating;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class RatingRepository {
    private final List<Rating> ratings = new ArrayList<>();
    private long nextId = 1;

    public Rating save(Rating rating) {
        if (rating.getId() == null) {
            rating.setId(nextId++);
        } else {
            ratings.removeIf(r -> Objects.equals(r.getId(), rating.getId()));
        }
        ratings.add(rating);
        return rating;
    }

    public void remove(Long id) {
        ratings.removeIf(r -> Objects.equals(r.getId(), id));
    }

    public List<Rating> findAll() {
        return new ArrayList<>(ratings);
    }

    public Optional<Rating> findById(Long id) {
        return ratings.stream()
                .filter(r -> Objects.equals(r.getId(), id))
                .findFirst();
    }
}