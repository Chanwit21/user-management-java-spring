package com.bbl.usr.management.service;

import com.bbl.usr.management.client.UserClient;
import com.bbl.usr.management.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserClient userClient;

    @InjectMocks
    private UserService userService;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user1 = new User();
        user1.setId(1L);
        user1.setName("Chanwit Pansila");
        user1.setEmail("bomb@gmail.com");

        user2 = new User();
        user2.setId(2L);
        user2.setName("BOMB Chanwit");
        user2.setEmail("chanwit@gmail.com");

        // mock init()
        when(userClient.getUserData()).thenReturn(Arrays.asList(user1, user2));
        userService.init();
    }

    @Test
    void testGetAllUser() {
        List<User> users = userService.getAllUser();
        assertEquals(2, users.size());
    }

    @Test
    void testGetUserById_found() {
        Optional<User> userOpt = userService.getUserById(1L);
        assertTrue(userOpt.isPresent());
        assertEquals("Chanwit Pansila", userOpt.get().getName());
    }

    @Test
    void testGetUserById_notFound() {
        Optional<User> userOpt = userService.getUserById(99L);
        assertFalse(userOpt.isPresent());
    }

    @Test
    void testUpdateUser_found() {
        User updated = new User();
        updated.setName("Updated BOMB");
        updated.setUsername("Chanwit");
        updated.setEmail("Chan@gmail.com");
        updated.setPhone("123456789");
        updated.setWebsite("bomb.com");

        Optional<User> result = userService.updateUser(1L, updated);

        assertTrue(result.isPresent());
        assertEquals("Updated BOMB", result.get().getName());
        assertEquals("Chanwit", result.get().getUsername());
    }

    @Test
    void testUpdateUser_notFound() {
        User updated = new User();
        updated.setName("Nobody");

        Optional<User> result = userService.updateUser(99L, updated);

        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteUser_notFound() {
        boolean deleted = userService.deleteUser(99L);
        assertFalse(deleted);
        assertEquals(2, userService.getAllUser().size());
    }
}
