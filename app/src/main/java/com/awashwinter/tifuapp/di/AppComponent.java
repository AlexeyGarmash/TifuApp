package com.awashwinter.tifuapp.di;


import com.awashwinter.tifuapp.experimental.ui.home.HomeViewModel;
import com.awashwinter.tifuapp.ui.login.LoginViewModel;
import com.awashwinter.tifuapp.usecases.UseCaseGetPosts;
import com.awashwinter.tifuapp.usecases.UseCasePostActions;


import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {LoginRepositoryModule.class, UseCasePostsModule.class, UseCasePostActionsModule.class, TifuRepositoryModule.class})
@Singleton
public interface AppComponent {

    void injectLoginRepository(LoginViewModel loginViewModel);
    void injectUseCasePosts(HomeViewModel mainViewModel);
    void injectUseCasePostActions(HomeViewModel mainViewModel);
    void injectTifuRepositoryPosts(UseCaseGetPosts useCaseGetPosts);
    void injectTifuRepositoryActions(UseCasePostActions useCaseGetPosts);
}
