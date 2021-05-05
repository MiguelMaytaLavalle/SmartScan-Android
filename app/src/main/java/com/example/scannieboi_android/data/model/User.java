package com.example.scannieboi_android.data.model;

public class User {

    private String username;
    private String password;
    private String email;
    private String authToken;

    public User(String username, String password, String authToken) {
        this.username = username;
        this.password = password;
        this.authToken = authToken;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAuthToken() {
        return authToken;
    }
}
