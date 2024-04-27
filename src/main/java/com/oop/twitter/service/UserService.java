package com.oop.twitter.service;

import com.oop.twitter.model.Post;
import com.oop.twitter.model.User;
import com.oop.twitter.repository.PostRepository;
import com.oop.twitter.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public UserService(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public User getUser(Long userID) {
        return userRepository.findById(userID).orElseThrow(() -> new RuntimeException("User does not exist"));
    }

    // Add the getAllPosts method here
    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByDateDesc();
    }

    public List<User> getAllUsers() {
        Iterable<User> usersIterable = userRepository.findAll();
        List<User> usersList = new ArrayList<>();
        usersIterable.forEach(usersList::add);
        return usersList;
    }
}