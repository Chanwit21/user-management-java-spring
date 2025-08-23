package com.bbl.usr.management.controller;

import com.bbl.usr.management.entities.User;
import com.bbl.usr.management.model.GetAllUserResp;
import com.bbl.usr.management.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        user1 = new User();
        user1.setId(1L);
        user1.setName("Chanwit Pansila");
        user1.setUsername("chanwit");
        user1.setEmail("chanwit@gmail.com");

        user2 = new User();
        user2.setId(2L);
        user2.setName("BOMB Chanwit");
        user2.setUsername("bomb");
        user2.setEmail("bomb@gmail.com");
    }

    @Test
    void testGetAllUser_found() throws Exception {
        when(userService.getAllUser()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users", hasSize(2)))
                .andExpect(jsonPath("$.users[0].name", is("Chanwit Pansila")));
    }

    @Test
    void testGetAllUser_notFound() throws Exception {
        when(userService.getAllUser()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/users"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.users", hasSize(0)));
    }

    @Test
    void testGetUserById_found() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.of(user1));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Chanwit Pansila")));
    }

    @Test
    void testGetUserById_notFound() throws Exception {
        when(userService.getUserById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateUser() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(user1);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Chanwit Pansila")));
    }

    @Test
    void testUpdateUser_found() throws Exception {
        when(userService.updateUser(any(Long.class), any(User.class))).thenReturn(Optional.of(user2));

        mockMvc.perform(put("/users/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("BOMB Chanwit")))
                .andExpect(jsonPath("$.username", is("bomb")))
                .andExpect(jsonPath("$.email", is("bomb@gmail.com")));
    }

    @Test
    void testUpdateUser_notFound() throws Exception {
        when(userService.updateUser(any(Long.class), any(User.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/users/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user2)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteUser_found() throws Exception {
        when(userService.deleteUser(1L)).thenReturn(true);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteUser_notFound() throws Exception {
        when(userService.deleteUser(1L)).thenReturn(false);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNotFound());
    }
}
