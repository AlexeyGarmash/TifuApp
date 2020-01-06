package com.awashwinter.tifuapp.usecases;

import com.awashwinter.tifuapp.base.TifuApp;
import com.awashwinter.tifuapp.base.Utils;
import com.awashwinter.tifuapp.data.TifuRepository;
import com.awashwinter.tifuapp.data.model.TifuPost;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

public class UseCasePostActions {

    @Inject
    TifuRepository tifuRepository;
    private FirebaseUser firebaseUser;

    public UseCasePostActions(FirebaseUser firebaseUser){
        TifuApp.getAppComponent().injectTifuRepositoryActions(this);
        this.firebaseUser = firebaseUser;
    }

    public void createTifuPost(String title, String content){
        tifuRepository.createPost(title, content);
    }

    public void setLike(TifuPost tifuPost){
        tifuRepository.setLikeOrDis(Utils.getUser(firebaseUser), tifuPost, "likes");
    }

    public void setDislike(TifuPost tifuPost){
        tifuRepository.setLikeOrDis(Utils.getUser(firebaseUser), tifuPost, "dislikes");
    }
}
