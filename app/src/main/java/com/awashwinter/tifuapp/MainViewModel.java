package com.awashwinter.tifuapp;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.awashwinter.tifuapp.base.TifuApp;
import com.awashwinter.tifuapp.base.Utils;
import com.awashwinter.tifuapp.data.TifuRepository;
import com.awashwinter.tifuapp.data.model.TifuPost;
import com.bumptech.glide.util.Util;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class MainViewModel extends ViewModel {


    public enum NetState {
        START,
        FINISH
    }
    private MutableLiveData<Boolean> userLoggedIn = new MutableLiveData<>();
    private MutableLiveData<List<TifuPost>> listMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<NetState> netStateMutableLiveData = new MutableLiveData<>();
    private FirebaseAuth firebaseAuth;


    @Inject
    TifuRepository tifuRepository;

    public MainViewModel(){
        firebaseAuth = TifuApp.getFirebaseAuth();
        netStateMutableLiveData.setValue(NetState.START);
        TifuApp.getAppComponent().injectTifuRepository(this);
        tifuRepository.getData();
        tifuRepository.setOnDataChangedListener(new TifuRepository.OnDataChangedListener() {
            @Override
            public void onDataChangeStarted() {

                Log.d("DATA_CHANGE_START", "CHANGE START");
            }

            @Override
            public void onDataChanged(List<TifuPost> accessedTifuPosts) {
                Log.d("DATA_CHANGE_FINISH", "CHANGE FINISH");
                netStateMutableLiveData.setValue(NetState.FINISH);
                listMutableLiveData.setValue(accessedTifuPosts);
            }
        });
        firebaseAuth.addAuthStateListener(firebaseAuth1 -> {
            if(firebaseAuth1.getCurrentUser() == null){
                userLoggedIn.setValue(false);
            }
        });
    }

    public LiveData<Boolean> getUserLoggedIn() {
        return userLoggedIn;
    }

    public LiveData<List<TifuPost>> getListMutableLiveData() {
        return listMutableLiveData;
    }

    public void checkUserIsLoggedIn(){
        userLoggedIn.setValue(firebaseAuth.getCurrentUser() != null);
    }

    public void logout() {
        firebaseAuth.signOut();
    }

    public void addTifu(String title, String content) {
        tifuRepository.createPost(title, content);
    }

    public LiveData<NetState> getNetStateMutableLiveData() {
        return netStateMutableLiveData;
    }

    public void setLike(TifuPost post) {
        tifuRepository.setLikeOrDis(Utils.getUser(Objects.requireNonNull(firebaseAuth.getCurrentUser())), post,"likes");
    }

    public void setDisLike(TifuPost post) {
        tifuRepository.setLikeOrDis(Utils.getUser(Objects.requireNonNull(firebaseAuth.getCurrentUser())), post,"dislikes");
    }


    @Override
    protected void onCleared() {
        Log.d("MAIN_VIEW_MODEL", "onCleared()");
        super.onCleared();
    }
}
