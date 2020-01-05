package com.awashwinter.tifuapp.base;

import android.app.Application;

import com.awashwinter.tifuapp.di.AppComponent;
import com.awashwinter.tifuapp.di.DaggerAppComponent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class TifuApp extends Application {

    private static AppComponent appComponent;
    private static FirebaseAuth firebaseAuth;
    private static FirebaseDatabase database;



    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.create();
        firebaseAuth = FirebaseAuth.getInstance();
        database =  FirebaseDatabase.getInstance();
    }

    public static AppComponent getAppComponent(){
        return appComponent;
    }
    public static FirebaseAuth getFirebaseAuth(){
        return firebaseAuth;
    }
    public static FirebaseDatabase getDatabase(){
        return database;
    }

}
