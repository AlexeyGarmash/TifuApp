package com.awashwinter.tifuapp.di;

import com.awashwinter.tifuapp.data.TifuRepository;
import com.awashwinter.tifuapp.usecases.UseCaseGetPosts;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class UseCasePostsModule {


    @Provides
    UseCaseGetPosts  provideUseCaseGetPosts(){
        return new UseCaseGetPosts();
    }




}
