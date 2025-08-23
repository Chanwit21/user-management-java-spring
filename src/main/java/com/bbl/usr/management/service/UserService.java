package com.bbl.usr.management.service;

import com.bbl.usr.management.client.UserClient;
import com.bbl.usr.management.entities.User;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    List<User> users = new ArrayList<>();

    private final UserClient userClient;

    @Autowired
    public UserService(UserClient userClient) {
        this.userClient = userClient;
    }

    @PostConstruct
    public void init() {
        users = userClient.getUserData();
        System.out.println(users);
    }

    public List<User> getAllUser(){
        return users;
    }

    public Optional<User> getUserById(Long userId) {
        return users.stream()
                .filter(u -> Objects.equals(u.getId(), userId))
                .findFirst();
    }

    public User createUser(User user) {
        user.setId((long) (users.size() + 1));
        users.add(user);
        return user;
    }

    public Optional<User> updateUser(Long userId, User updatedUser) {
        return getUserById(userId).map(existingUser -> {
            existingUser.setName(updatedUser.getName());
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPhone(updatedUser.getPhone());
            existingUser.setWebsite(updatedUser.getWebsite());
            return existingUser;
        });
    }

    public boolean deleteUser(Long userId) {
        return users.removeIf(user -> user.getId() == userId);
    }
}
