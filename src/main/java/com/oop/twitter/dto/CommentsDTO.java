package com.oop.twitter.dto;

public class CommentsDTO {
    private Long commentID;
    private String commentBody;
    private UserDTO commentCreator;

    // getters and setters...
    public void setCommentID(Long commentID) {
        this.commentID = commentID;
    }

    public Long getCommentID() {
        return this.commentID;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

    public String getCommentBody() {
        return this.commentBody;
    }

    public void setCommentCreator(UserDTO commentCreator) {
        this.commentCreator = commentCreator;
    }
}