package com.ssin.todolist.ui.login.module;

import com.google.firebase.auth.FirebaseAuth;
import com.ssin.todolist.ui.login.interactor.LoginInteractor;
import com.ssin.todolist.ui.login.interactor.LoginInteractorImpl;
import com.ssin.todolist.ui.login.presenter.LoginPresenter;
import com.ssin.todolist.ui.login.presenter.LoginPresenterImpl;
import com.ssin.todolist.ui.login.view.LoginView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by SS-In on 2018-07-16.
 */

@Module
public class LoginModule {
    private LoginView loginView;

    public LoginModule(LoginView loginView) {
        this.loginView = loginView;
    }

    @Singleton
    @Provides
    public LoginView getLoginView() {
        return loginView;
    }

    @Singleton
    @Provides
    public LoginPresenter provideLoginPresenter(LoginView loginView, LoginInteractor loginInteractor) {
        return new LoginPresenterImpl(loginView, loginInteractor);
    }

    @Singleton
    @Provides
    public LoginInteractor provideLoginInteractor(FirebaseAuth auth) {
        return new LoginInteractorImpl(auth);
    }
}
