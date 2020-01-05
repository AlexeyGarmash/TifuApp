package com.awashwinter.tifuapp.data.model;

import android.net.Uri;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String userId;
    private String displayName;
    private String avatar;

    public LoggedInUser() {
    }

    public LoggedInUser(String userId, String displayName, Uri avatar) {
        this.userId = userId;
        this.displayName = displayName;
        this.avatar = avatar.toString();
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
