package com.example.restaurantrating.controller;

import org.springframework.boot.test.mock.mockito.MockBean;
import com.example.restaurantrating.dto.ReviewRequest;
import com.example.restaurantrating.dto.ReviewResponse;
import com.example.restaurantrating.service.RatingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RatingService ratingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllReviews_ShouldReturnList() throws Exception {
        ReviewResponse response = new ReviewResponse(1L, 1L, 5, "Отлично");
        when(ratingService.findAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].visitorId").value(1));
    }

    @Test
    void getReviewById_ShouldReturnReview() throws Exception {
        ReviewResponse response = new ReviewResponse(1L, 1L, 5, "Отлично");
        when(ratingService.findByVisitorAndRestaurant(1L, 1L)).thenReturn(response);

        mockMvc.perform(get("/api/reviews/1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(5));
    }

    @Test
    void createReview_ShouldReturnCreated() throws Exception {
        ReviewRequest request = new ReviewRequest(1L, 1L, 5, "Отлично");
        ReviewResponse response = new ReviewResponse(1L, 1L, 5, "Отлично");
        when(ratingService.save(any(ReviewRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.visitorId").value(1));
    }

    @Test
    void updateReview_ShouldReturnOk() throws Exception {
        ReviewRequest request = new ReviewRequest(1L, 1L, 4, "Хорошо");
        ReviewResponse response = new ReviewResponse(1L, 1L, 4, "Хорошо");
        when(ratingService.update(any(ReviewRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(4));
    }

    @Test
    void deleteReview_ShouldReturnNoContent() throws Exception {
        doNothing().when(ratingService).remove(1L, 1L);

        mockMvc.perform(delete("/api/reviews/1/1"))
                .andExpect(status().isNoContent());
    }
}
