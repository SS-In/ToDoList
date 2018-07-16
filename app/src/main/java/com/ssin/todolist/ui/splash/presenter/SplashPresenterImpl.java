package com.ssin.todolist.ui.splash.presenter;

import com.ssin.todolist.ui.splash.interactor.SplashInteractor;
import com.ssin.todolist.ui.splash.view.SplashView;

/**
 * Created by SS-In on 2018-07-16.
 */

public class SplashPresenterImpl implements SplashPresenter {
    private SplashView splashView;
    private SplashInteractor splashInteractor;

    public SplashPresenterImpl(SplashView splashView, SplashInteractor splashInteractor) {
        this.splashView = splashView;
        this.splashInteractor = splashInteractor;
    }

    @Override
    public void onUserCheckingAuth() {
        if (splashInteractor.checkUserIsSignedIn())
            splashView.navigateIntoMainActivity();
    }
}
