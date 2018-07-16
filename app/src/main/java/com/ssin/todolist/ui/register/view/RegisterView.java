package com.ssin.todolist.ui.register.view;

/**
 * Created by SS-In on 2018-07-16.
 */

public interface RegisterView {
    void setDisplayNameError();

    void setPasswordError();

    void setEmailError();

    void setConfirmPasswordError();

    void setPasswordNotMatchError();

    void setInvalidEmailFormatError();

    void showProgress();

    void hideProgress();

    void resetErrors();

    void navigateToMain();

    void setGeneralError(String message);
}
