package com.ssin.todolist.ui.register.interactor;

/**
 * Created by SS-In on 2018-07-16.
 */

public interface RegisterInteractor {
    interface OnFinishedListener {
        void onSuccess();

        void onEmailError();

        void onInvalidEmailError();

        void onPasswordError();

        void onConfirmPasswordError();

        void onDisplayNameError();

        void onPasswordsNotMatch();

        void onServerError(String message);
    }

    void register(String email, String password, String confirmPassword, String displayName, OnFinishedListener onFinishedListener);
}
