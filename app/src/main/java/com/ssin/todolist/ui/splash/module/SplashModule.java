package com.ssin.todolist.ui.splash.module;

import com.google.firebase.auth.FirebaseAuth;
import com.ssin.todolist.ui.splash.interactor.SplashInteractor;
import com.ssin.todolist.ui.splash.interactor.SplashInteractorImpl;
import com.ssin.todolist.ui.splash.presenter.SplashPresenter;
import com.ssin.todolist.ui.splash.presenter.SplashPresenterImpl;
import com.ssin.todolist.ui.splash.view.SplashView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by SS-In on 2018-07-16.
 */
@Module
public class SplashModule {
    private SplashView splashView;

    public SplashModule(SplashView splashView) {
        this.splashView = splashView;
    }

    @Singleton
    @Provides
    public SplashView getSplashView() {
        return splashView;
    }

    @Singleton
    @Provides
    public SplashPresenter provideSplashPresenter(SplashView splashView, SplashInteractor splashInteractor) {
        return new SplashPresenterImpl(splashView, splashInteractor);
    }

    @Singleton
    @Provides
    public SplashInteractor provideSplashInteractor(FirebaseAuth auth) {
        return new SplashInteractorImpl(auth);
    }
}
