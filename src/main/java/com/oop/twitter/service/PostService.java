package com.oop.twitter.service;

import com.oop.twitter.model.Post;
import com.oop.twitter.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }

    public Post getPost(Long postID) {
        return postRepository.findById(postID).orElseThrow(() -> new RuntimeException("Post does not exist"));
    }
}