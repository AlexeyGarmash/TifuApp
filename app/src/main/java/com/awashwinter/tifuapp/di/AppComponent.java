package com.awashwinter.tifuapp.di;

import com.awashwinter.tifuapp.MainViewModel;
import com.awashwinter.tifuapp.data.TifuRepository;
import com.awashwinter.tifuapp.experimental.ui.home.HomeViewModel;
import com.awashwinter.tifuapp.ui.login.LoginViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {LoginRepositoryModule.class, TifuRepositoryModule.class})
@Singleton
public interface AppComponent {

    void injectLoginRepository(LoginViewModel loginViewModel);
    void injectTifuRepository(HomeViewModel mainViewModel);
}
