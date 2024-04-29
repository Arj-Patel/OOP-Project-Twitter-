package com.oop.twitter.controller;

import com.oop.twitter.model.Comments;
import com.oop.twitter.model.Post;
import com.oop.twitter.model.User;
import com.oop.twitter.repository.CommentsRepository;
import com.oop.twitter.repository.PostRepository;
import com.oop.twitter.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;

import java.util.Optional;

@RestController
@RequestMapping("/comment")
public class CommentsController {

    private final CommentsRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentsController(CommentsRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody Map<String, Object> payload) {
        Integer postId = (Integer) payload.get("postID");
        Integer userId = (Integer) payload.get("userID");
        String commentBody = (String) payload.get("commentBody");

        Optional<Post> postOptional = postRepository.findById(Long.valueOf(postId));
        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));

        Map<String, String> errorResponse = new HashMap<>();
        if (!userOptional.isPresent()) {
            errorResponse.put("Error", "User does not exist");
            return ResponseEntity.badRequest().body(errorResponse);
        } else if (!postOptional.isPresent()) {
            errorResponse.put("Error", "Post does not exist");
            return ResponseEntity.badRequest().body(errorResponse);
        } else {
            Comments comment = new Comments();
            comment.setCommentBody(commentBody);
            comment.setPost(postOptional.get());
            comment.setUser(userOptional.get());
            commentRepository.save(comment);
            return ResponseEntity.ok("Comment created successfully");
        }
    }

    @GetMapping
    public ResponseEntity<?> getComment(@RequestParam Long commentID) {
        Optional<Comments> commentOptional = commentRepository.findById(commentID);
        if (commentOptional.isPresent()) {
            Comments comment = commentOptional.get();
            Map<String, Object> commentMap = new HashMap<>();
            commentMap.put("commentID", comment.getCommentID());
            commentMap.put("commentBody", comment.getCommentBody());

            Map<String, Object> userMap = new HashMap<>();
            userMap.put("userID", comment.getUser().getUserID());
            userMap.put("name", comment.getUser().getName());

            commentMap.put("commentCreator", userMap);

            return ResponseEntity.ok(commentMap);
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Error", "Comment does not exist");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PatchMapping
    public ResponseEntity<?> editComment(@RequestBody Comments comment) {
        Optional<Comments> commentOptional = commentRepository.findById(comment.getCommentID());
        if (commentOptional.isPresent()) {
            Comments existingComment = commentOptional.get();
            existingComment.setCommentBody(comment.getCommentBody());
            commentRepository.save(existingComment);
            return ResponseEntity.ok("Comment edited successfully");
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Error", "Comment does not exist");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteComment(@RequestParam Long commentID) {
        Optional<Comments> commentOptional = commentRepository.findById(commentID);
        if (commentOptional.isPresent()) {
            commentRepository.deleteById(commentID);
            return ResponseEntity.ok("Comment deleted");
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Error", "Comment does not exist");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}