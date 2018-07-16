package com.ssin.todolist.ui.register.presenter;

/**
 * Created by SS-In on 2018-07-16.
 */

public interface RegisterPresenter {
    void validateRegisterCredentials(String email, String password, String confirmPassword, String displayName);
}
