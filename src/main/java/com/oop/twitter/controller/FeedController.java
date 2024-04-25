package com.oop.twitter.controller;

import com.oop.twitter.model.Post;
import com.oop.twitter.repository.PostRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class FeedController {

    private final PostRepository postRepository;

    public FeedController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllPosts() {
        Iterable<Post> postsIterable = postRepository.findAll();
        List<Post> posts = new ArrayList<>();
        postsIterable.forEach(posts::add);

        List<Map<String, Object>> postMaps = posts.stream()
                .sorted(Comparator.comparing(Post::getPostID).reversed())
                .map(this::postToMap)
                .collect(Collectors.toList());

        return ResponseEntity.ok(postMaps);
    }

    private Map<String, Object> postToMap(Post post) {
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
}