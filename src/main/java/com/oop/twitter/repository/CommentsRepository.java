package com.oop.twitter.repository;

import com.oop.twitter.model.Comments;
import org.springframework.data.repository.CrudRepository;


public interface CommentsRepository extends CrudRepository<Comments, Long> {
}