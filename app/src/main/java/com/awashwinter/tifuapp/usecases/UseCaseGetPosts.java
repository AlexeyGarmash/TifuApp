package com.awashwinter.tifuapp.usecases;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.awashwinter.tifuapp.base.TifuApp;
import com.awashwinter.tifuapp.data.TifuRepository;
import com.awashwinter.tifuapp.data.model.TifuPost;
import com.awashwinter.tifuapp.data.model.comparators.DateComparator;
import com.awashwinter.tifuapp.experimental.ui.home.HomeViewModel;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class UseCaseGetPosts implements TifuRepository.OnDataChangedListener {


    @Inject
    TifuRepository tifuRepository;
    private SortType currentSortType;

    private MutableLiveData<List<TifuPost>> postsMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<HomeViewModel.NetState> netStateMutableLiveData = new MutableLiveData<>();

    public UseCaseGetPosts(){
        TifuApp.getAppComponent().injectTifuRepositoryPosts(this);
        currentSortType = SortType.DATE_DESCENDING;
        this.tifuRepository.setOnDataChangedListener(this);
    }


    public void getPostsWithSort(SortType sortType){
        netStateMutableLiveData.setValue(HomeViewModel.NetState.START);
        currentSortType = sortType;
        tifuRepository.getDataOnce();

    }

    public void getPostsAlways(){
        netStateMutableLiveData.setValue(HomeViewModel.NetState.START);
        tifuRepository.getData();
    }


    @Override
    public void onDataChangeStarted() {

    }

    @Override
    public void onDataChanged(List<TifuPost> tifuPosts) {
        computeSort(tifuPosts);
        netStateMutableLiveData.setValue(HomeViewModel.NetState.FINISH);
    }

    private void computeSort(List<TifuPost> tifuPosts){
        Collections.sort(tifuPosts, new DateComparator(currentSortType));
        postsMutableLiveData.setValue(tifuPosts);
    }

    public LiveData<List<TifuPost>> getPostsMutableLiveData() {
        return postsMutableLiveData;
    }

    public LiveData<HomeViewModel.NetState> getNetStateMutableLiveData() {
        return netStateMutableLiveData;
    }
}
