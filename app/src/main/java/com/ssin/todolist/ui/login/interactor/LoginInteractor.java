package com.ssin.todolist.ui.login.interactor;

/**
 * Created by SS-In on 2018-07-16.
 */

public interface LoginInteractor {
    void login(String email, String password, OnLoginFinishedListener listener);

    interface OnLoginFinishedListener {
        void onSuccess();

        void onError(String error);

        void onEmailError();

        void onPasswordError();
    }
}
