package com.example.restaurantrating.controller;

import com.example.restaurantrating.dto.RestaurantRequest;
import com.example.restaurantrating.dto.RestaurantResponse;
import com.example.restaurantrating.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@Tag(name = "Restaurants", description = "Управление ресторанами")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    @Operation(summary = "Получить все рестораны")
    public List<RestaurantResponse> getAllRestaurants() {
        return restaurantService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить ресторан по ID")
    public ResponseEntity<RestaurantResponse> getRestaurantById(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Создать новый ресторан")
    public ResponseEntity<RestaurantResponse> createRestaurant(@RequestBody RestaurantRequest request) {
        return new ResponseEntity<>(restaurantService.save(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить ресторан")
    public ResponseEntity<RestaurantResponse> updateRestaurant(@PathVariable Long id, @RequestBody RestaurantRequest request) {
        return ResponseEntity.ok(restaurantService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить ресторан")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        restaurantService.remove(id);
        return ResponseEntity.noContent().build();
    }
}