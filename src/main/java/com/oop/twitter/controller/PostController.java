package com.oop.twitter.controller;

import com.oop.twitter.model.Post;
import com.oop.twitter.model.User;
import com.oop.twitter.repository.PostRepository;
import com.oop.twitter.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.Date;

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
    public ResponseEntity<String> createPost(@RequestBody Post post) {
        Optional<User> userOptional = userRepository.findById(post.getUser().getUserID());
        if (userOptional.isPresent()) {
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
            return ResponseEntity.ok(postOptional.get());
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
            return ResponseEntity.ok("Post updated successfully");
        } else {
            return ResponseEntity.badRequest().body("Post does not exist");
        }
    }

    // Delete a post
    @DeleteMapping
    public ResponseEntity<String> deletePost(@RequestBody Long postID) {
        Optional<Post> postOptional = postRepository.findById(postID);
        if (postOptional.isPresent()) {
            postRepository.deleteById(postID);
            return ResponseEntity.ok("Post deleted");
        } else {
            return ResponseEntity.badRequest().body("Post does not exist");
        }
    }

}