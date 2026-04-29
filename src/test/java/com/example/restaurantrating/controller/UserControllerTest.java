package com.example.restaurantrating.controller;

import org.springframework.boot.test.mock.mockito.MockBean;
import com.example.restaurantrating.dto.UserRequest;
import com.example.restaurantrating.dto.UserResponse;
import com.example.restaurantrating.service.VisitorService;
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

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VisitorService visitorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllUsers_ShouldReturnList() throws Exception {
        UserResponse response = new UserResponse(1L, "Иван", 30, "М");
        when(visitorService.findAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Иван"));
    }

    @Test
    void getUserById_ShouldReturnUser() throws Exception {
        UserResponse response = new UserResponse(1L, "Иван", 30, "М");
        when(visitorService.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void createUser_ShouldReturnCreated() throws Exception {
        UserRequest request = new UserRequest("Иван", 30, "М");
        UserResponse response = new UserResponse(1L, "Иван", 30, "М");
        when(visitorService.save(any(UserRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void updateUser_ShouldReturnOk() throws Exception {
        UserRequest request = new UserRequest("Иван", 31, "М");
        UserResponse response = new UserResponse(1L, "Иван", 31, "М");
        when(visitorService.update(eq(1L), any(UserRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.age").value(31));
    }

    @Test
    void deleteUser_ShouldReturnNoContent() throws Exception {
        doNothing().when(visitorService).remove(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }
}
