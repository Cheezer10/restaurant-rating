package com.example.restaurantrating.service;

import com.example.restaurantrating.dto.ReviewRequest;
import com.example.restaurantrating.dto.ReviewResponse;
import com.example.restaurantrating.entity.Rating;
import com.example.restaurantrating.entity.RatingId;
import com.example.restaurantrating.entity.Restaurant;
import com.example.restaurantrating.repository.RatingRepository;
import com.example.restaurantrating.repository.RestaurantRepository;
import com.example.restaurantrating.repository.VisitorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.restaurantrating.entity.Visitor;
import com.example.restaurantrating.entity.Restaurant;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RatingService {
    private final RatingRepository ratingRepository;
    private final RestaurantRepository restaurantRepository;
    private final VisitorRepository visitorRepository; // для проверки существования

    public RatingService(RatingRepository ratingRepository,
                         RestaurantRepository restaurantRepository,
                         VisitorRepository visitorRepository) {
        this.ratingRepository = ratingRepository;
        this.restaurantRepository = restaurantRepository;
        this.visitorRepository = visitorRepository;
    }

    private ReviewResponse toResponse(Rating rating) {
        return new ReviewResponse(
                rating.getId().getVisitorId(),
                rating.getId().getRestaurantId(),
                rating.getRating(),
                rating.getComment()
        );
    }

    private Rating toEntity(ReviewRequest request) {
        Visitor visitor = visitorRepository.findById(request.visitorId())
                .orElseThrow(() -> new RuntimeException("Visitor not found with id: " + request.visitorId()));
        Restaurant restaurant = restaurantRepository.findById(request.restaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + request.restaurantId()));

        RatingId id = new RatingId(request.visitorId(), request.restaurantId());
        Rating rating = new Rating();
        rating.setId(id);
        rating.setRating(request.rating());
        rating.setComment(request.comment());
        rating.setVisitor(visitor);
        rating.setRestaurant(restaurant);
        return rating;
    }

    public ReviewResponse save(ReviewRequest request) {
        if (ratingRepository.existsById(new RatingId(request.visitorId(), request.restaurantId()))) {
            throw new RuntimeException("Review already exists for this visitor and restaurant");
        }
        Rating rating = toEntity(request);
        Rating saved = ratingRepository.save(rating);
        updateRestaurantAverageRating(saved.getId().getRestaurantId());
        return toResponse(saved);
    }

    public ReviewResponse update(ReviewRequest request) {
        RatingId id = new RatingId(request.visitorId(), request.restaurantId());
        Rating existing = ratingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rating not found"));
        existing.setRating(request.rating());
        existing.setComment(request.comment());
        Rating updated = ratingRepository.save(existing);
        updateRestaurantAverageRating(updated.getId().getRestaurantId());
        return toResponse(updated);
    }

    public void remove(Long visitorId, Long restaurantId) {
        RatingId id = new RatingId(visitorId, restaurantId);
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rating not found"));
        Long restaurantIdForUpdate = rating.getId().getRestaurantId();
        ratingRepository.deleteById(id);
        updateRestaurantAverageRating(restaurantIdForUpdate);
    }

    public List<ReviewResponse> findAll() {
        return ratingRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ReviewResponse findByVisitorAndRestaurant(Long visitorId, Long restaurantId) {
        RatingId id = new RatingId(visitorId, restaurantId);
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rating not found"));
        return toResponse(rating);
    }

    private void updateRestaurantAverageRating(Long restaurantId) {
        List<Rating> ratings = ratingRepository.findByRestaurantId(restaurantId);
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