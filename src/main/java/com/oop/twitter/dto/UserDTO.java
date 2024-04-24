package com.oop.twitter.dto;

public class UserDTO {
    private Long userID;
    private String name;

    // getters and setters...
    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Long getUserID() {
        return this.userID;
    }

    public void setName(String name) {
        this.name = name;
    }
}