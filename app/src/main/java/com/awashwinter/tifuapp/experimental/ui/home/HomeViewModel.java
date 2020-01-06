package com.awashwinter.tifuapp.experimental.ui.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.awashwinter.tifuapp.base.TifuApp;
import com.awashwinter.tifuapp.data.model.TifuPost;
import com.awashwinter.tifuapp.usecases.SortType;
import com.awashwinter.tifuapp.usecases.UseCasePostActions;
import com.awashwinter.tifuapp.usecases.UseCaseGetPosts;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import javax.inject.Inject;

public class HomeViewModel extends ViewModel {



    public enum NetState {
        START,
        FINISH
    }




    @Inject
    UseCaseGetPosts useCaseGetPosts;
    @Inject
    UseCasePostActions useCasePostActions;


    public HomeViewModel() {


        TifuApp.getAppComponent().injectUseCasePosts(this);
        TifuApp.getAppComponent().injectUseCasePostActions(this);
        useCaseGetPosts.getPostsAlways();
        /*tifuRepository.setOnDataChangedListener(new TifuRepository.OnDataChangedListener() {
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
        });*/
    }

    public LiveData<List<TifuPost>> getListMutableLiveData() {
        return useCaseGetPosts.getPostsMutableLiveData();
    }

    public void addTifu(String title, String content) {
        useCasePostActions.createTifuPost(title, content);
    }

    public void applySort(SortType sortType) {
        useCaseGetPosts.getPostsWithSort(sortType);
    }

    public LiveData<NetState> getNetStateMutableLiveData() {
        return useCaseGetPosts.getNetStateMutableLiveData();
    }

    public void setLike(TifuPost post) {
        useCasePostActions.setLike(post);
    }

    public void setDisLike(TifuPost post) {
        useCasePostActions.setDislike(post);
    }


    @Override
    protected void onCleared() {
        Log.d("MAIN_VIEW_MODEL", "onCleared()");
        super.onCleared();
    }

}