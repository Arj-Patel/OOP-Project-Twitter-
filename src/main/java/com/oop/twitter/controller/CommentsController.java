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
    public ResponseEntity<String> createComment(@RequestBody Map<String, Object> payload) {
        Long postId = Long.valueOf(payload.get("post").toString());
        Long userId = Long.valueOf(payload.get("user").toString());
        String commentBody = (String) payload.get("commentBody");

        Optional<Post> postOptional = postRepository.findById(postId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (postOptional.isPresent() && userOptional.isPresent()) {
            Comments comment = new Comments();
            comment.setCommentBody(commentBody);
            comment.setPost(postOptional.get());
            comment.setUser(userOptional.get());
            commentRepository.save(comment);
            return ResponseEntity.ok("Comment created successfully");
        } else {
            return ResponseEntity.badRequest().body("Post or User does not exist");
        }
    }

    @GetMapping
    public ResponseEntity<Object> getComment(@RequestParam Long commentID) {
        Optional<Comments> commentOptional = commentRepository.findById(commentID);
        if (commentOptional.isPresent()) {
            return ResponseEntity.ok(commentOptional.get());
        } else {
            return ResponseEntity.badRequest().body("Comment does not exist");
        }
    }

    @PatchMapping
    public ResponseEntity<String> editComment(@RequestBody Comments comment) {
        Optional<Comments> commentOptional = commentRepository.findById(comment.getCommentID());
        if (commentOptional.isPresent()) {
            Comments existingComment = commentOptional.get();
            existingComment.setCommentBody(comment.getCommentBody());
            commentRepository.save(existingComment);
            return ResponseEntity.ok("Comment updated successfully");
        } else {
            return ResponseEntity.badRequest().body("Comment does not exist");
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteComment(@RequestBody Map<String, Integer> payload) {
        Integer commentID = payload.get("commentID");
        Optional<Comments> commentOptional = commentRepository.findById(Long.valueOf(commentID));
        if (commentOptional.isPresent()) {
            commentRepository.deleteById(Long.valueOf(commentID));
            return ResponseEntity.ok("Comment deleted");
        } else {
            return ResponseEntity.badRequest().body("Comment does not exist");
        }
    }
}