package com.oop.twitter.controller;

import com.oop.twitter.model.User;
import com.oop.twitter.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        User existingUser = userService.findByEmail(user.getEmail());
        if (existingUser == null) {
            return ResponseEntity.badRequest().body("User does not exist");
        }
        if (!existingUser.getPassword().equals(user.getPassword())) {
            return ResponseEntity.badRequest().body("Username/Password Incorrect");
        }
        return ResponseEntity.ok("Login Successful");
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        User existingUser = userService.findByEmail(user.getEmail());
        if (existingUser != null) {
            return new ResponseEntity<>("Forbidden, Account already exists", HttpStatus.FORBIDDEN);
        }
        userService.save(user);
        return new ResponseEntity<>("Account Creation Successful", HttpStatus.CREATED);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUser(@RequestParam Long userID) {
        try {
            User user = userService.getUser(userID);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist");
        }
    }

}