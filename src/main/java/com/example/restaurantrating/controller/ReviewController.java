package com.example.restaurantrating.controller;

import com.example.restaurantrating.dto.ReviewRequest;
import com.example.restaurantrating.dto.ReviewResponse;
import com.example.restaurantrating.service.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@Tag(name = "Reviews", description = "Управление отзывами (составной ключ: visitorId + restaurantId)")
public class ReviewController {

    private final RatingService ratingService;

    public ReviewController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @GetMapping
    @Operation(summary = "Получить все отзывы")
    public List<ReviewResponse> getAllReviews() {
        return ratingService.findAll();
    }

    @GetMapping("/{visitorId}/{restaurantId}")
    @Operation(summary = "Получить отзыв по visitorId и restaurantId")
    public ResponseEntity<ReviewResponse> getReviewById(@PathVariable Long visitorId,
                                                        @PathVariable Long restaurantId) {
        return ResponseEntity.ok(ratingService.findByVisitorAndRestaurant(visitorId, restaurantId));
    }

    @PostMapping
    @Operation(summary = "Создать новый отзыв")
    public ResponseEntity<ReviewResponse> createReview(@RequestBody ReviewRequest request) {
        return new ResponseEntity<>(ratingService.save(request), HttpStatus.CREATED);
    }

    @PutMapping
    @Operation(summary = "Обновить отзыв (visitorId и restaurantId берутся из тела запроса)")
    public ResponseEntity<ReviewResponse> updateReview(@RequestBody ReviewRequest request) {
        return ResponseEntity.ok(ratingService.update(request));
    }

    @DeleteMapping("/{visitorId}/{restaurantId}")
    @Operation(summary = "Удалить отзыв по visitorId и restaurantId")
    public ResponseEntity<Void> deleteReview(@PathVariable Long visitorId,
                                             @PathVariable Long restaurantId) {
        ratingService.remove(visitorId, restaurantId);
        return ResponseEntity.noContent().build();
    }
}