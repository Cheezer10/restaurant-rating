package com.example.restaurantrating.service;

import com.example.restaurantrating.dto.UserRequest;
import com.example.restaurantrating.dto.UserResponse;
import com.example.restaurantrating.entity.Visitor;
import com.example.restaurantrating.repository.VisitorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitorServiceTest {

    @Mock
    private VisitorRepository visitorRepository;

    @InjectMocks
    private VisitorService visitorService;

    private Visitor visitor;
    private UserRequest request;

    @BeforeEach
    void setUp() {
        visitor = new Visitor(1L, "Иван", 30, "М");
        request = new UserRequest("Иван", 30, "М");
    }

    @Test
    void save_ShouldSaveAndReturnResponse() {
        when(visitorRepository.save(any(Visitor.class))).thenReturn(visitor);

        UserResponse response = visitorService.save(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Иван");
        verify(visitorRepository, times(1)).save(any(Visitor.class));
    }

    @Test
    void update_ShouldUpdateAndReturnResponse() {
        when(visitorRepository.findById(1L)).thenReturn(Optional.of(visitor));
        when(visitorRepository.save(any(Visitor.class))).thenReturn(visitor);

        UserResponse response = visitorService.update(1L, request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Иван");
        verify(visitorRepository, times(1)).findById(1L);
        verify(visitorRepository, times(1)).save(any(Visitor.class));
    }

    @Test
    void update_WhenNotFound_ShouldThrowException() {
        when(visitorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> visitorService.update(1L, request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Visitor not found with id: 1");
    }

    @Test
    void remove_ShouldDelete() {
        doNothing().when(visitorRepository).deleteById(1L);

        visitorService.remove(1L);

        verify(visitorRepository, times(1)).deleteById(1L);
    }

    @Test
    void findAll_ShouldReturnList() {
        when(visitorRepository.findAll()).thenReturn(List.of(visitor));

        List<UserResponse> responses = visitorService.findAll();

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).id()).isEqualTo(1L);
        verify(visitorRepository, times(1)).findAll();
    }

    @Test
    void findById_ShouldReturnResponse() {
        when(visitorRepository.findById(1L)).thenReturn(Optional.of(visitor));

        UserResponse response = visitorService.findById(1L);

        assertThat(response.id()).isEqualTo(1L);
    }

    @Test
    void findById_WhenNotFound_ShouldThrowException() {
        when(visitorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> visitorService.findById(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Visitor not found");
    }
}
