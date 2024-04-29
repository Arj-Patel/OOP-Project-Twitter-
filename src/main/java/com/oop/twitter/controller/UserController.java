package com.oop.twitter.controller;

import com.oop.twitter.model.User;
import com.oop.twitter.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
//import com.oop.twitter.model.Post;
//import org.springframework.web.servlet.View;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class UserController {

    private final UserService userService;
//    private final View error;

    public UserController(UserService userService) {
        this.userService = userService;
//        this.error = error;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        User existingUser = userService.findByEmail(user.getEmail());
        if (existingUser == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Error", "User does not exist");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        if (!existingUser.getPassword().equals(user.getPassword())) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Error", "Username/Password Incorrect");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        return ResponseEntity.ok("Login Successful");
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        User existingUser = userService.findByEmail(user.getEmail());
        if (existingUser != null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Error", "Forbidden, Account already exists");
            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        }
        userService.save(user);
        return new ResponseEntity<>("Account Creation Successful", HttpStatus.CREATED);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUser(@RequestParam Long userID) {
        try {
            User user = userService.getUser(userID);
            Map<String, Object> response = new HashMap<>();
            response.put("name", user.getName());
            response.put("userID", user.getUserID());
            response.put("email", user.getEmail());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Error", "User does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

//    private Map<String, Object> getStringObjectMap(Post post) {
//        Map<String, Object> postMap = new HashMap<>();
//        postMap.put("postID", post.getPostID());
//        postMap.put("postBody", post.getPostBody());
//        postMap.put("date", post.getDate());
//
//        List<Map<String, Object>> commentMaps = post.getComments().stream().map(comment -> {
//            Map<String, Object> commentMap = new HashMap<>();
//            commentMap.put("commentID", comment.getCommentID());
//            commentMap.put("commentBody", comment.getCommentBody());
//
//            Map<String, Object> userMap = new HashMap<>();
//            userMap.put("userID", comment.getUser().getUserID());
//            userMap.put("name", comment.getUser().getName());
//
//            commentMap.put("commentCreator", userMap);
//
//            return commentMap;
//        }).collect(Collectors.toList());
//
//        postMap.put("comments", commentMaps);
//
//        return postMap;
//    }

    @GetMapping("/users")
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<Map<String, Object>> userMaps = users.stream().map(user -> {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("name", user.getName());
            userMap.put("userID", user.getUserID());
            userMap.put("email", user.getEmail());
            return userMap;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(userMaps);
    }

}