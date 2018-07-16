package com.ssin.todolist.ui.register.module;

import com.google.firebase.auth.FirebaseAuth;
import com.ssin.todolist.ui.register.interactor.RegisterInteractor;
import com.ssin.todolist.ui.register.interactor.RegisterInteractorImpl;
import com.ssin.todolist.ui.register.presenter.RegisterPresenter;
import com.ssin.todolist.ui.register.presenter.RegisterPresenterImpl;
import com.ssin.todolist.ui.register.view.RegisterView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by SS-In on 2018-07-16.
 */

@Module
public class RegisterModule {
    private RegisterView registerView;

    public RegisterModule(RegisterView registerView) {
        this.registerView = registerView;
    }

    @Singleton
    @Provides
    public RegisterView getRegisterView() {
        return registerView;
    }

    @Singleton
    @Provides
    public RegisterPresenter provideRegisterPresenter(RegisterView registerView, RegisterInteractor registerInteractor) {
        return new RegisterPresenterImpl(registerView, registerInteractor);
    }

    @Singleton
    @Provides
    public RegisterInteractor provideRegisterInteractor(FirebaseAuth auth) {
        return new RegisterInteractorImpl(auth);
    }
}
