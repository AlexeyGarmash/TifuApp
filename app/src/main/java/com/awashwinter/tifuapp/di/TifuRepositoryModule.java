package com.awashwinter.tifuapp.di;

import com.awashwinter.tifuapp.data.TifuRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class TifuRepositoryModule {

    @Provides
    @Singleton
    TifuRepository provideTifuRepository(){
        return new TifuRepository();
    }

}
