package com.oop.twitter.controller;

import com.oop.twitter.model.Post;
import com.oop.twitter.model.User;
import com.oop.twitter.repository.PostRepository;
import com.oop.twitter.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/post")
public class PostController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostController(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<String> createPost(@RequestBody Map<String, Object> payload) {
        Long userId = Long.valueOf(payload.get("userID").toString());
        String postBody = payload.get("postBody").toString();
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            Post post = new Post();
            post.setUser(userOptional.get());
            post.setPostBody(postBody);
            post.setDate(new Date()); // set the date here
            postRepository.save(post);
            return ResponseEntity.ok("Post created successfully");
        } else {
            return ResponseEntity.badRequest().body("User does not exist");
        }
    }

    @GetMapping
    public ResponseEntity<Object> getPost(@RequestParam Long postID) {
        Optional<Post> postOptional = postRepository.findById(postID);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            List<Map<String, Object>> commentDTOs = post.getComments().stream().map(comment -> {
                Map<String, Object> commentMap = new HashMap<>();
                commentMap.put("commentID", comment.getCommentID());
                commentMap.put("commentBody", comment.getCommentBody());

                Map<String, Object> userMap = new HashMap<>();
                userMap.put("userID", comment.getUser().getUserID());
                userMap.put("name", comment.getUser().getName());

                commentMap.put("commentCreator", userMap);

                return commentMap;
            }).collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("postID", post.getPostID());
            response.put("postBody", post.getPostBody());
            response.put("date", post.getDate());
            response.put("comments", commentDTOs);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body("Post does not exist");
        }
    }

    // Edit an existing post
    @PatchMapping
    public ResponseEntity<String> editPost(@RequestBody Post post) {
        Optional<Post> postOptional = postRepository.findById(post.getPostID());
        if (postOptional.isPresent()) {
            Post existingPost = postOptional.get();
            existingPost.setPostBody(post.getPostBody());
            postRepository.save(existingPost);
            return ResponseEntity.ok("Post edited successfully");
        } else {
            return ResponseEntity.badRequest().body("Post does not exist");
        }
    }

    // Delete a post
    @DeleteMapping
    public ResponseEntity<String> deletePost(@RequestBody Map<String, Integer> payload) {
        Long postID = Long.valueOf(payload.get("postID"));
        Optional<Post> postOptional = postRepository.findById(postID);
        if (postOptional.isPresent()) {
            postRepository.deleteById(postID);
            return ResponseEntity.ok("Post deleted");
        } else {
            return ResponseEntity.badRequest().body("Post does not exist");
        }
    }

}