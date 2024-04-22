package com.oop.twitter.model;

import jakarta.persistence.*;

@Entity
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long commentID;

    private String commentBody;

    @ManyToOne
    @JoinColumn(name = "postID") // changed from "postID" to "post_id"
    private Post post;

    @ManyToOne
    @JoinColumn(name = "userID") // assuming the column in the user table is "user_id"
    private User user;

    // getters and setters
    public Long getCommentID() {
        return this.commentID;
    }

    public void setCommentID(Long commentID) {
        this.commentID = commentID;
    }

    public String getCommentBody() {
        return commentBody;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}