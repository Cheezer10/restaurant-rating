package com.example.restaurantrating.service;

import com.example.restaurantrating.entity.Rating;
import com.example.restaurantrating.entity.Restaurant;
import com.example.restaurantrating.repository.RatingRepository;
import com.example.restaurantrating.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;
    private final RestaurantRepository restaurantRepository;

    public RatingService(RatingRepository ratingRepository, RestaurantRepository restaurantRepository) {
        this.ratingRepository = ratingRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public Rating save(Rating rating) {
        Rating savedRating = ratingRepository.save(rating);
        updateRestaurantAverageRating(savedRating.getRestaurantId());
        return savedRating;
    }

    public void remove(Long id) {
        Optional<Rating> ratingOpt = ratingRepository.findById(id);
        if (ratingOpt.isPresent()) {
            Long restaurantId = ratingOpt.get().getRestaurantId();
            ratingRepository.remove(id);
            updateRestaurantAverageRating(restaurantId);
        }
    }

    public List<Rating> findAll() {
        return ratingRepository.findAll();
    }

    private void updateRestaurantAverageRating(Long restaurantId) {
        List<Rating> restaurantRatings = ratingRepository.findAll().stream()
                .filter(r -> r.getRestaurantId().equals(restaurantId))
                .toList();

        BigDecimal newAverage;
        if (restaurantRatings.isEmpty()) {
            newAverage = BigDecimal.ZERO;
        } else {
            double avg = restaurantRatings.stream()
                    .mapToInt(Rating::getRating)
                    .average()
                    .orElse(0.0);
            newAverage = BigDecimal.valueOf(avg).setScale(2, RoundingMode.HALF_UP);
        }

        restaurantRepository.findById(restaurantId).ifPresent(restaurant -> {
            restaurant.setAverageRating(newAverage);
            restaurantRepository.save(restaurant);
        });
    }
}