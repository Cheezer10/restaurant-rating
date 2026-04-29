package com.example.restaurantrating.service;

import com.example.restaurantrating.dto.ReviewRequest;
import com.example.restaurantrating.dto.ReviewResponse;
import com.example.restaurantrating.entity.Rating;
import com.example.restaurantrating.entity.RatingId;
import com.example.restaurantrating.entity.Restaurant;
import com.example.restaurantrating.entity.Visitor;
import com.example.restaurantrating.repository.RatingRepository;
import com.example.restaurantrating.repository.RestaurantRepository;
import com.example.restaurantrating.repository.VisitorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private VisitorRepository visitorRepository;

    @InjectMocks
    private RatingService ratingService;

    private Visitor visitor;
    private Restaurant restaurant;
    private Rating rating;
    private ReviewRequest request;

    @BeforeEach
    void setUp() {
        visitor = new Visitor(1L, "Иван", 30, "М");
        restaurant = new Restaurant(1L, "Итальянский дворик", "Уютно", null, new BigDecimal("1500"), new BigDecimal("0"));
        RatingId id = new RatingId(1L, 1L);
        rating = new Rating(id, 5, "Отлично", visitor, restaurant);
        request = new ReviewRequest(1L, 1L, 5, "Отлично");
    }

    @Test
    void save_ShouldSaveAndReturnResponse_WhenNotExists() {
        when(visitorRepository.findById(1L)).thenReturn(Optional.of(visitor));
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(ratingRepository.existsById(any(RatingId.class))).thenReturn(false);
        when(ratingRepository.save(any(Rating.class))).thenReturn(rating);
        when(ratingRepository.findByRestaurantId(anyLong())).thenReturn(List.of(rating));

        ReviewResponse response = ratingService.save(request);

        assertThat(response.visitorId()).isEqualTo(1L);
        assertThat(response.restaurantId()).isEqualTo(1L);
        assertThat(response.rating()).isEqualTo(5);
        verify(ratingRepository, times(1)).save(any(Rating.class));
        verify(restaurantRepository, times(2)).findById(1L);
        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
    }

    @Test
    void save_WhenAlreadyExists_ShouldThrowException() {
        when(ratingRepository.existsById(any(RatingId.class))).thenReturn(true);

        assertThatThrownBy(() -> ratingService.save(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Review already exists");
    }

    @Test
    void update_ShouldUpdateAndReturnResponse() {
        when(ratingRepository.findById(any(RatingId.class))).thenReturn(Optional.of(rating));
        when(ratingRepository.save(any(Rating.class))).thenReturn(rating);
        when(ratingRepository.findByRestaurantId(anyLong())).thenReturn(List.of(rating));

        ReviewResponse response = ratingService.update(request);

        assertThat(response.rating()).isEqualTo(5);
        verify(ratingRepository, times(1)).save(any(Rating.class));
    }

    @Test
    void update_WhenNotFound_ShouldThrowException() {
        when(ratingRepository.findById(any(RatingId.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ratingService.update(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Rating not found");
    }

    @Test
    void remove_ShouldDeleteAndUpdateAverageRating() {
        when(ratingRepository.findById(any(RatingId.class))).thenReturn(Optional.of(rating));
        when(ratingRepository.findByRestaurantId(1L)).thenReturn(List.of());
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        ratingService.remove(1L, 1L);

        verify(ratingRepository, times(1)).deleteById(any(RatingId.class));
        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
    }

    @Test
    void findAll_ShouldReturnList() {
        when(ratingRepository.findAll()).thenReturn(List.of(rating));

        List<ReviewResponse> responses = ratingService.findAll();

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).visitorId()).isEqualTo(1L);
    }

    @Test
    void findByVisitorAndRestaurant_ShouldReturnResponse() {
        when(ratingRepository.findById(any(RatingId.class))).thenReturn(Optional.of(rating));

        ReviewResponse response = ratingService.findByVisitorAndRestaurant(1L, 1L);

        assertThat(response.visitorId()).isEqualTo(1L);
    }
}
