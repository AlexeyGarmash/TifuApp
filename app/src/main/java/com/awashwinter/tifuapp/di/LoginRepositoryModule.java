package com.awashwinter.tifuapp.di;

import com.awashwinter.tifuapp.data.LoginRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class LoginRepositoryModule {

    @Provides
    @Singleton
    LoginRepository provideRepository(){
        return new LoginRepository();
    }
}
