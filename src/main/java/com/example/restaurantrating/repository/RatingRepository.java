package com.example.restaurantrating.repository;

import com.example.restaurantrating.entity.Rating;
import com.example.restaurantrating.entity.RatingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, RatingId> {
    List<Rating> findByRestaurantId(Long restaurantId);
}