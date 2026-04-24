package com.example.restaurantrating.controller;

import com.example.restaurantrating.dto.UserRequest;
import com.example.restaurantrating.dto.UserResponse;
import com.example.restaurantrating.service.VisitorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Управление посетителями")
public class UserController {

    private final VisitorService visitorService;

    public UserController(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    @GetMapping
    @Operation(summary = "Получить всех посетителей")
    public List<UserResponse> getAllUsers() {
        return visitorService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить посетителя по ID")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(visitorService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Создать нового посетителя")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request) {
        return new ResponseEntity<>(visitorService.save(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить существующего посетителя")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserRequest request) {
        return ResponseEntity.ok(visitorService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить посетителя")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        visitorService.remove(id);
        return ResponseEntity.noContent().build();
    }
}