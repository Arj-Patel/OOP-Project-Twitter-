package com.oop.twitter.repository;

import com.oop.twitter.model.Post;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Long> {
    List<Post> findAllByOrderByDateDesc();
}