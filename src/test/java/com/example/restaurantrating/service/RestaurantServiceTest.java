package com.example.restaurantrating.service;

import com.example.restaurantrating.dto.RestaurantRequest;
import com.example.restaurantrating.dto.RestaurantResponse;
import com.example.restaurantrating.entity.Restaurant;
import com.example.restaurantrating.enums.CuisineType;
import com.example.restaurantrating.repository.RestaurantRepository;
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
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    private Restaurant restaurant;
    private RestaurantRequest request;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant(1L, "Итальянский дворик", "Уютное место", CuisineType.ITALIAN,
                new BigDecimal("1500"), BigDecimal.ZERO);
        request = new RestaurantRequest("Итальянский дворик", "Уютное место", CuisineType.ITALIAN,
                new BigDecimal("1500"));
    }

    @Test
    void save_ShouldSaveAndReturnResponse() {
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        RestaurantResponse response = restaurantService.save(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Итальянский дворик");
        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
    }

    @Test
    void update_ShouldUpdateAndReturnResponse() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        RestaurantResponse response = restaurantService.update(1L, request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Итальянский дворик");
        verify(restaurantRepository, times(1)).findById(1L);
        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
    }

    @Test
    void update_WhenNotFound_ShouldThrowException() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> restaurantService.update(1L, request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Restaurant not found");
    }

    @Test
    void remove_ShouldDelete() {
        doNothing().when(restaurantRepository).deleteById(1L);

        restaurantService.remove(1L);

        verify(restaurantRepository, times(1)).deleteById(1L);
    }

    @Test
    void findAll_ShouldReturnList() {
        when(restaurantRepository.findAll()).thenReturn(List.of(restaurant));

        List<RestaurantResponse> responses = restaurantService.findAll();

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).id()).isEqualTo(1L);
        verify(restaurantRepository, times(1)).findAll();
    }

    @Test
    void findById_ShouldReturnResponse() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        RestaurantResponse response = restaurantService.findById(1L);

        assertThat(response.id()).isEqualTo(1L);
    }

    @Test
    void findById_WhenNotFound_ShouldThrowException() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> restaurantService.findById(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Restaurant not found");
    }
}
