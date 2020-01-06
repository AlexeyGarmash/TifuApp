package com.awashwinter.tifuapp.di;

import com.awashwinter.tifuapp.base.TifuApp;
import com.awashwinter.tifuapp.data.TifuRepository;
import com.awashwinter.tifuapp.usecases.UseCasePostActions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class UseCasePostActionsModule {

    @Provides
    UseCasePostActions provideUseCasePostActions(FirebaseUser firebaseUser){
        return new UseCasePostActions(firebaseUser);
    }

    @Provides
    @Singleton
    FirebaseUser provideFirebaseUser(FirebaseAuth firebaseAuth){
        return firebaseAuth.getCurrentUser();
    }

    @Provides
    @Singleton
    FirebaseAuth provideFirebaseAuth(){
        return TifuApp.getFirebaseAuth();
    }
}
