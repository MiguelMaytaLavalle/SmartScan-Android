package com.example.scannieboi_android.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String userID;
    private String displayName;

    public LoggedInUser(String userID, String displayName) {
        this.userID = userID;
        this.displayName = displayName;
    }

    public String getUserID() {
        return userID;
    }

    public String getDisplayName() {
        return displayName;
    }
}