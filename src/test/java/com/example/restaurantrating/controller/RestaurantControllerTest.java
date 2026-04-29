package com.example.restaurantrating.controller;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.example.restaurantrating.dto.RestaurantRequest;
import com.example.restaurantrating.dto.RestaurantResponse;
import com.example.restaurantrating.enums.CuisineType;
import com.example.restaurantrating.service.RestaurantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestaurantController.class)
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestaurantService restaurantService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllRestaurants_ShouldReturnList() throws Exception {
        RestaurantResponse response = new RestaurantResponse(1L, "Итальянский дворик", "Уютно",
                CuisineType.ITALIAN, new BigDecimal("1500"), new BigDecimal("4.5"));
        when(restaurantService.findAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Итальянский дворик"));
    }

    @Test
    void getRestaurantById_ShouldReturnRestaurant() throws Exception {
        RestaurantResponse response = new RestaurantResponse(1L, "Итальянский дворик", "Уютно",
                CuisineType.ITALIAN, new BigDecimal("1500"), new BigDecimal("4.5"));
        when(restaurantService.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/restaurants/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void createRestaurant_ShouldReturnCreated() throws Exception {
        RestaurantRequest request = new RestaurantRequest("Итальянский дворик", "Уютно",
                CuisineType.ITALIAN, new BigDecimal("1500"));
        RestaurantResponse response = new RestaurantResponse(1L, "Итальянский дворик", "Уютно",
                CuisineType.ITALIAN, new BigDecimal("1500"), BigDecimal.ZERO);
        when(restaurantService.save(any(RestaurantRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void updateRestaurant_ShouldReturnOk() throws Exception {
        RestaurantRequest request = new RestaurantRequest("Новое название", "Новое описание",
                CuisineType.ITALIAN, new BigDecimal("2000"));
        RestaurantResponse response = new RestaurantResponse(1L, "Новое название", "Новое описание",
                CuisineType.ITALIAN, new BigDecimal("2000"), BigDecimal.ZERO);
        when(restaurantService.update(eq(1L), any(RestaurantRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/restaurants/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Новое название"));
    }

    @Test
    void deleteRestaurant_ShouldReturnNoContent() throws Exception {
        doNothing().when(restaurantService).remove(1L);

        mockMvc.perform(delete("/api/restaurants/1"))
                .andExpect(status().isNoContent());
    }
}
