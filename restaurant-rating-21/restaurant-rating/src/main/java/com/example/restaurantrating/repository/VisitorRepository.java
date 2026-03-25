package com.example.restaurantrating.repository;

import com.example.restaurantrating.entity.Visitor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class VisitorRepository {
    private final List<Visitor> visitors = new ArrayList<>();
    private long nextId = 1;

    public Visitor save(Visitor visitor) {
        if (visitor.getId() == null) {
            visitor.setId(nextId++);
        } else {
            visitors.removeIf(v -> Objects.equals(v.getId(), visitor.getId()));
        }
        visitors.add(visitor);
        return visitor;
    }

    public void remove(Long id) {
        visitors.removeIf(v -> Objects.equals(v.getId(), id));
    }

    public List<Visitor> findAll() {
        return new ArrayList<>(visitors);
    }

    public Optional<Visitor> findById(Long id) {
        return visitors.stream()
                .filter(v -> Objects.equals(v.getId(), id))
                .findFirst();
    }
}