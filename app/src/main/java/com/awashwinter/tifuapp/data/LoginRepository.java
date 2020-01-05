package com.awashwinter.tifuapp.data;

import androidx.annotation.NonNull;

import com.awashwinter.tifuapp.base.TifuApp;
import com.awashwinter.tifuapp.base.Utils;
import com.awashwinter.tifuapp.data.model.LoggedInUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {



    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;

    private FirebaseAuth firebaseAuth;
    private OnLoginResultListener onLoginResultListener;
    private OnLogoutResultListener onLogoutResultListener;

    // private constructor : singleton access
    public LoginRepository() {
        firebaseAuth = TifuApp.getFirebaseAuth();
        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    //onLogoutResultListener.logoutResult("Logout success");
                }
            }
        });
    }


    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;

    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public void login(String username, String password) {
        // handle login
        firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                    assert firebaseUser != null;
                    LoggedInUser loggedInUser = new LoggedInUser(firebaseUser.getUid(), firebaseUser.getDisplayName(), firebaseUser.getPhotoUrl());
                    onLoginResultListener.loginResult(new Result.Success<>(loggedInUser));
                }
                else{
                    onLoginResultListener.loginResult(new Result.Error(task.getException()));
                }
            }
        });
    }

    public void register(String username, final String password, final String displayName){
        firebaseAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(displayName)
                            .setPhotoUri(Utils.getAvatarUriFromUrl(String.format(displayName)))
                            .build();

                    if (firebaseUser != null) {
                        firebaseUser.updateProfile(profileUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                assert firebaseUser != null;
                                LoggedInUser loggedInUser = new LoggedInUser(firebaseUser.getUid(), firebaseUser.getDisplayName(), firebaseUser.getPhotoUrl());
                                onLoginResultListener.loginResult(new Result.Success<>(loggedInUser));
                            }
                        });
                    }


                }else{
                    onLoginResultListener.loginResult(new Result.Error(task.getException()));
                }
            }
        });
    }


    public void setOnLoginResultListener(OnLoginResultListener onLoginResultListener) {
        this.onLoginResultListener = onLoginResultListener;
    }

}
