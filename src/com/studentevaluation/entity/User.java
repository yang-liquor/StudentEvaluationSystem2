package com.studentevaluation.entity;

public class User {
    private String userId;
    private String password;
    private String role;
    private String refId;

    public User() {}
    public User(String userId, String password, String role, String refId) {
        this.userId = userId;
        this.password = password;
        this.role = role;
        this.refId = refId;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }
}