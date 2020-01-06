package com.awashwinter.tifuapp.experimental;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.awashwinter.tifuapp.base.TifuApp;
import com.awashwinter.tifuapp.base.Utils;
import com.awashwinter.tifuapp.data.model.LoggedInUser;
import com.awashwinter.tifuapp.usecases.SortType;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class HomeMainViewModel extends ViewModel {

    private MutableLiveData<LoggedInUser> loggedInUser = new MutableLiveData<>();
    private MutableLiveData<Boolean> isUserLoggedIn = new MutableLiveData<>();
    private FirebaseAuth firebaseAuth;


    private MutableLiveData<SortType> actionSortMutableLiveData = new MutableLiveData<>();

    public HomeMainViewModel(){
        firebaseAuth = TifuApp.getFirebaseAuth();
        firebaseAuth.addAuthStateListener(firebaseAuth1 -> {
            if(firebaseAuth1.getCurrentUser() == null){
                isUserLoggedIn.setValue(false);
            }
        });
    }


    public void setCurrentUser(){
        loggedInUser.setValue(Utils.getUser(Objects.requireNonNull(firebaseAuth.getCurrentUser())));
    }

    public void checkUserIsLoggedIn(){
        isUserLoggedIn.setValue(firebaseAuth.getCurrentUser() != null);
    }

    public void logout() {
        firebaseAuth.signOut();
    }

    public void sendSortType(SortType sortType){
        actionSortMutableLiveData.setValue(sortType);
    }

    public LiveData<LoggedInUser> getLoggedInUser() {
        return loggedInUser;
    }
    public LiveData<Boolean> getIsUserLoggedIn() {
        return isUserLoggedIn;
    }

    public LiveData<SortType> getActionSortMutableLiveData() {
        return actionSortMutableLiveData;
    }
}
