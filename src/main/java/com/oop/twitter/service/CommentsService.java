//package com.oop.twitter.service;
//
//import com.oop.twitter.model.Comments;
//import com.oop.twitter.repository.CommentsRepository;
//import org.springframework.stereotype.Service;
//
//@Service
//public class CommentsService {
//
//    private final CommentsRepository commentsRepository;
//
//    public CommentsService(CommentsRepository commentsRepository) {
//        this.commentsRepository = commentsRepository;
//    }
//
//    public Comments save(Comments comment) {
//        return commentsRepository.save(comment);
//    }
//
//    public Comments getComment(Long commentID) {
//        return commentsRepository.findById(commentID).orElseThrow(() -> new RuntimeException("Comment does not exist"));
//    }
//}