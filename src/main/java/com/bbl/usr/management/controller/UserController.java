package com.bbl.usr.management.controller;

import com.bbl.usr.management.entities.User;
import com.bbl.usr.management.model.GetAllUserResp;
import com.bbl.usr.management.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("users")
public class UserController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<GetAllUserResp> getAllUser(){
        List<User> users = null;
        GetAllUserResp response = null;
        HttpStatus status = HttpStatus.OK;
        try {
            users = userService.getAllUser();
        } catch (Exception e) {
            log.error("Exception getAllUser : {}", e.getMessage(), e);
        } finally {
            response = new GetAllUserResp(users);
            if(CollectionUtils.isEmpty(users)){
                status = HttpStatus.NOT_FOUND;
            }
        }
        return new ResponseEntity<>(response, status);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = null;
        HttpStatus status = HttpStatus.OK;
        try {
            Optional<User> userOpt = userService.getUserById(id);
            if(userOpt.isPresent()){
                user = userOpt.get();
            }
        } catch (Exception e) {
            log.error("Exception getUserById : {}", e.getMessage(), e);
        } finally {
            if(ObjectUtils.isEmpty(user)){
                status = HttpStatus.NOT_FOUND;
            }
        }
        return new ResponseEntity<>(user, status);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        User userResp = null;
        HttpStatus status = HttpStatus.CREATED;
        try {
            if (isInvalidUser(user)) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Validation failed: name, username, and email must not be null or empty.");
            }
            userResp = userService.createUser(user);
        } catch (Exception e) {
            log.error("Exception getUserById : {}", e.getMessage(), e);
        }
        return new ResponseEntity<>(userResp, status);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        User userResp = null;
        HttpStatus status = HttpStatus.OK;
        try {
            if (isInvalidUser(user)) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Validation failed: name, username, and email must not be null or empty.");
            }
            Optional<User> userOpt = userService.updateUser(id, user);
            if(userOpt.isPresent()){
                userResp = userOpt.get();
            }
        } catch (Exception e) {
            log.error("Exception getUserById : {}", e.getMessage(), e);
        } finally {
            if(ObjectUtils.isEmpty(userResp)){
                status = HttpStatus.NOT_FOUND;
            }
        }
        return new ResponseEntity<>(userResp, status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        boolean deleted = false;
        HttpStatus status = HttpStatus.OK;
        String resp = "Delete Success Fully";
        try {
            deleted = userService.deleteUser(id);
        } catch (Exception e) {
            log.error("Exception deleteUser : {}", e.getMessage(), e);
        } finally {
            if(!deleted){
                resp = "User Not Found";
                status = HttpStatus.NOT_FOUND;
            }
        }
        return new ResponseEntity<>(resp,status);
    }

    private boolean isInvalidUser(User user) {
        return (user == null ||
                ObjectUtils.isEmpty(user.getName()) ||
                ObjectUtils.isEmpty(user.getUsername()) ||
                ObjectUtils.isEmpty(user.getEmail()));
    }

}
