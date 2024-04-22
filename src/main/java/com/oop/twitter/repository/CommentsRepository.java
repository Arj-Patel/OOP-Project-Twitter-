package com.oop.twitter.repository;

import com.oop.twitter.model.Comments;
import com.oop.twitter.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentsRepository extends CrudRepository<Comments, Long> {
    List<Comments> findByUser(User user);
}