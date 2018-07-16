package com.ssin.todolist.ui.register.presenter;

import com.ssin.todolist.ui.register.interactor.RegisterInteractor;
import com.ssin.todolist.ui.register.view.RegisterView;

/**
 * Created by SS-In on 2018-07-16.
 */

public class RegisterPresenterImpl implements RegisterPresenter, RegisterInteractor.OnFinishedListener {
    public RegisterPresenterImpl(RegisterView registerView, RegisterInteractor registerInteractor) {
        this.registerView = registerView;
        this.registerInteractor = registerInteractor;
    }

    private RegisterView registerView;
    private RegisterInteractor registerInteractor;

    @Override
    public void validateRegisterCredentials(String email, String password, String confirmPassword, String displayName) {
        registerView.showProgress();
        registerView.resetErrors();
        registerInteractor.register(email, password, confirmPassword, displayName, this);
    }

    @Override
    public void onSuccess() {
        registerView.hideProgress();
        registerView.navigateToMain();
    }

    @Override
    public void onEmailError() {
        registerView.hideProgress();
        registerView.setEmailError();
    }

    @Override
    public void onPasswordError() {
        registerView.hideProgress();
        registerView.setPasswordError();
    }

    @Override
    public void onDisplayNameError() {
        registerView.hideProgress();
        registerView.setDisplayNameError();
    }

    @Override
    public void onPasswordsNotMatch() {
        registerView.hideProgress();
        registerView.setPasswordNotMatchError();
    }

    @Override
    public void onConfirmPasswordError() {
        registerView.hideProgress();
        registerView.setConfirmPasswordError();
    }

    @Override
    public void onInvalidEmailError() {
        registerView.hideProgress();
        registerView.setInvalidEmailFormatError();
    }

    @Override
    public void onServerError(String message) {
        registerView.hideProgress();
        registerView.setGeneralError(message);
    }
}
