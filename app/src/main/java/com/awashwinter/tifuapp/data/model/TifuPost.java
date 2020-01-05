package com.awashwinter.tifuapp.data.model;

import com.awashwinter.tifuapp.base.TifuApp;
import com.awashwinter.tifuapp.base.Utils;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TifuPost implements Comparable<TifuPost> {

    private String postId;
    private String title;
    private String content;
    private boolean isAccessed;
    private boolean isModerated;
    private LoggedInUser user;

    private Long timestamp;
    private Map<String, LoggedInUser> likes;
    private Map<String, LoggedInUser> dislikes;

    public TifuPost() {
        likes= new HashMap<>();
        dislikes = new HashMap<>();
    }

    public TifuPost(String id, String title, String content, boolean isAcc, boolean isModer){
        postId = id;
        this.title = title;
        this.content = content;
        isAccessed = isAcc;
        isModerated = isModer;
        likes= new HashMap<>();
        dislikes = new HashMap<>();
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isAccessed() {
        return isAccessed;
    }

    public void setAccessed(boolean accessed) {
        isAccessed = accessed;
    }

    public boolean isModerated() {
        return isModerated;
    }

    public void setModerated(boolean moderated) {
        isModerated = moderated;
    }

    public LoggedInUser getUser() {
        return user;
    }

    public void setUser(LoggedInUser user) {
        this.user = user;
    }

    public Map<String, LoggedInUser> getLikes() {
        return likes;
    }

    public void setLikes(HashMap<String, LoggedInUser> likes) {
        this.likes = likes;
    }

    public Map<String, LoggedInUser> getDislikes() {
        return dislikes;
    }

    public void setDislikes(Map<String, LoggedInUser> dislikes) {
        this.dislikes = dislikes;
    }


    public java.util.Map<String, String> getTimestamp() {
        return ServerValue.TIMESTAMP;
    }

    @Exclude
    public Long getTimestampLong() {
        return timestamp;
    }

    @Exclude
    public String getTimeString(){
        return Utils.getTime(timestamp);
    }

    @Exclude
    public boolean likeIsYour(){
        return likes.containsKey(Utils.getUser(Objects.requireNonNull(TifuApp.getFirebaseAuth().getCurrentUser())).getUserId());
    }

    @Exclude
    public boolean dislikeIsYour(){
        return dislikes.containsKey(Utils.getUser(Objects.requireNonNull(TifuApp.getFirebaseAuth().getCurrentUser())).getUserId());
    }

    @Override
    public int compareTo(TifuPost tifuPost) {
        return (this.timestamp - tifuPost.timestamp) > 0? -1:1;
    }

}
