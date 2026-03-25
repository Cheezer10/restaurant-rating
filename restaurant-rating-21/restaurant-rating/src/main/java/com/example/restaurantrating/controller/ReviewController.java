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
@Tag(name = "Reviews", description = "Управление отзывами")
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

    @GetMapping("/{id}")
    @Operation(summary = "Получить отзыв по ID")
    public ResponseEntity<ReviewResponse> getReviewById(@PathVariable Long id) {
        return ResponseEntity.ok(ratingService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Создать новый отзыв")
    public ResponseEntity<ReviewResponse> createReview(@RequestBody ReviewRequest request) {
        return new ResponseEntity<>(ratingService.save(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить отзыв")
    public ResponseEntity<ReviewResponse> updateReview(@PathVariable Long id, @RequestBody ReviewRequest request) {
        return ResponseEntity.ok(ratingService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить отзыв")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        ratingService.remove(id);
        return ResponseEntity.noContent().build();
    }
}