package com.oop.twitter.controller;

import com.oop.twitter.model.User;
import com.oop.twitter.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import com.oop.twitter.model.Post;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

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
            Map<String, Object> response = new HashMap<>();
            response.put("name", user.getName());
            response.put("userID", user.getUserID());
            response.put("email", user.getEmail());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist");
        }
    }

    private Map<String, Object> getStringObjectMap(Post post) {
        Map<String, Object> postMap = new HashMap<>();
        postMap.put("postID", post.getPostID());
        postMap.put("postBody", post.getPostBody());
        postMap.put("date", post.getDate());

        List<Map<String, Object>> commentMaps = post.getComments().stream().map(comment -> {
            Map<String, Object> commentMap = new HashMap<>();
            commentMap.put("commentID", comment.getCommentID());
            commentMap.put("commentBody", comment.getCommentBody());

            Map<String, Object> userMap = new HashMap<>();
            userMap.put("userID", comment.getUser().getUserID());
            userMap.put("name", comment.getUser().getName());

            commentMap.put("commentCreator", userMap);

            return commentMap;
        }).collect(Collectors.toList());

        postMap.put("comments", commentMaps);

        return postMap;
    }

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