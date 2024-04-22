package com.oop.twitter.service;

import com.oop.twitter.model.User;
import com.oop.twitter.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

}