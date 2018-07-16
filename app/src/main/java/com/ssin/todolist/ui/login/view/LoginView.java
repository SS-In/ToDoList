package com.ssin.todolist.ui.login.view;

/**
 * Created by SS-In on 2018-07-16.
 */

public interface LoginView {
    void navigateToMain();

    void setLoginError(String message);

    void showProgress();

    void hideProgress();

    void resetErrors();

    void setEmailError();

    void setPasswordError();
}
