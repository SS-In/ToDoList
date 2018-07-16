package com.ssin.todolist.ui.login.presenter;

import com.ssin.todolist.ui.login.interactor.LoginInteractor;
import com.ssin.todolist.ui.login.view.LoginView;

/**
 * Created by SS-In on 2018-07-16.
 */

public class LoginPresenterImpl implements LoginPresenter, LoginInteractor.OnLoginFinishedListener {
    private LoginView loginView;
    private LoginInteractor loginInteractor;

    public LoginPresenterImpl(LoginView loginView, LoginInteractor loginInteractor) {
        this.loginView = loginView;
        this.loginInteractor = loginInteractor;
    }

    @Override
    public void validateCredentials(String email, String password) {
        loginView.resetErrors();
        loginView.showProgress();
        loginInteractor.login(email, password, this);
    }

    @Override
    public void onSuccess() {
        loginView.hideProgress();
        loginView.navigateToMain();
    }

    @Override
    public void onError(String error) {
        loginView.hideProgress();
        loginView.setLoginError(error);
    }

    @Override
    public void onEmailError() {
        loginView.hideProgress();
        loginView.setEmailError();
    }

    @Override
    public void onPasswordError() {
        loginView.hideProgress();
        loginView.setPasswordError();
    }
}
