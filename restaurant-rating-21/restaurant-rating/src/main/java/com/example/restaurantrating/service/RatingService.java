package com.example.restaurantrating.service;

import com.example.restaurantrating.dto.ReviewRequest;
import com.example.restaurantrating.dto.ReviewResponse;
import com.example.restaurantrating.entity.Rating;
import com.example.restaurantrating.entity.Restaurant;
import com.example.restaurantrating.repository.RatingRepository;
import com.example.restaurantrating.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;
    private final RestaurantRepository restaurantRepository;

    public RatingService(RatingRepository ratingRepository, RestaurantRepository restaurantRepository) {
        this.ratingRepository = ratingRepository;
        this.restaurantRepository = restaurantRepository;
    }

    private ReviewResponse toResponse(Rating rating) {
        return new ReviewResponse(
                rating.getId(),
                rating.getVisitorId(),
                rating.getRestaurantId(),
                rating.getRating(),
                rating.getComment()
        );
    }

    private Rating toEntity(ReviewRequest request) {
        Rating rating = new Rating();
        rating.setVisitorId(request.visitorId());
        rating.setRestaurantId(request.restaurantId());
        rating.setRating(request.rating());
        rating.setComment(request.comment());
        return rating;
    }

    public ReviewResponse save(ReviewRequest request) {
        Rating rating = toEntity(request);
        Rating saved = ratingRepository.save(rating);
        updateRestaurantAverageRating(saved.getRestaurantId());
        return toResponse(saved);
    }

    public ReviewResponse update(Long id, ReviewRequest request) {
        Rating existing = ratingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rating not found"));
        existing.setVisitorId(request.visitorId());
        existing.setRestaurantId(request.restaurantId());
        existing.setRating(request.rating());
        existing.setComment(request.comment());
        Rating updated = ratingRepository.save(existing);
        updateRestaurantAverageRating(updated.getRestaurantId());
        return toResponse(updated);
    }

    public void remove(Long id) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rating not found"));
        Long restaurantId = rating.getRestaurantId();
        ratingRepository.remove(id);
        updateRestaurantAverageRating(restaurantId);
    }

    public List<ReviewResponse> findAll() {
        return ratingRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ReviewResponse findById(Long id) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rating not found"));
        return toResponse(rating);
    }

    private void updateRestaurantAverageRating(Long restaurantId) {
        List<Rating> ratings = ratingRepository.findAll().stream()
                .filter(r -> r.getRestaurantId().equals(restaurantId))
                .collect(Collectors.toList());

        BigDecimal newAverage;
        if (ratings.isEmpty()) {
            newAverage = BigDecimal.ZERO;
        } else {
            double avg = ratings.stream()
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