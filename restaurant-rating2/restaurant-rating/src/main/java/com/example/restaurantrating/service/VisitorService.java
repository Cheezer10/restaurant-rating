package com.example.restaurantrating.service;

import com.example.restaurantrating.entity.Visitor;
import com.example.restaurantrating.repository.VisitorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisitorService {
    private final VisitorRepository visitorRepository;

    public VisitorService(VisitorRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
    }

    public Visitor save(Visitor visitor) {
        return visitorRepository.save(visitor);
    }

    public void remove(Long id) {
        visitorRepository.remove(id);
    }

    public List<Visitor> findAll() {
        return visitorRepository.findAll();
    }
}