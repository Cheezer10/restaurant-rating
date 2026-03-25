package com.example.restaurantrating.service;

import com.example.restaurantrating.dto.UserRequest;
import com.example.restaurantrating.dto.UserResponse;
import com.example.restaurantrating.entity.Visitor;
import com.example.restaurantrating.repository.VisitorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VisitorService {
    private final VisitorRepository visitorRepository;

    public VisitorService(VisitorRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
    }

    private UserResponse toResponse(Visitor visitor) {
        return new UserResponse(visitor.getId(), visitor.getName(), visitor.getAge(), visitor.getGender());
    }

    private Visitor toEntity(UserRequest request) {
        Visitor visitor = new Visitor();
        visitor.setName(request.name());
        visitor.setAge(request.age());
        visitor.setGender(request.gender());
        return visitor;
    }

    // CRUD через DTO
    public UserResponse save(UserRequest request) {
        Visitor visitor = toEntity(request);
        Visitor saved = visitorRepository.save(visitor);
        return toResponse(saved);
    }

    public UserResponse update(Long id, UserRequest request) {
        Visitor existing = visitorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Visitor not found with id: " + id));
        existing.setName(request.name());
        existing.setAge(request.age());
        existing.setGender(request.gender());
        Visitor updated = visitorRepository.save(existing);
        return toResponse(updated);
    }

    public void remove(Long id) {
        visitorRepository.remove(id);
    }

    public List<UserResponse> findAll() {
        return visitorRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public UserResponse findById(Long id) {
        Visitor visitor = visitorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Visitor not found"));
        return toResponse(visitor);
    }
}