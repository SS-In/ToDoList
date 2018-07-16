package com.ssin.todolist.ui.splash.interactor;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by SS-In on 2018-07-16.
 */

public class SplashInteractorImpl implements SplashInteractor {
    private FirebaseAuth firebaseAuth;

    public SplashInteractorImpl(FirebaseAuth auth) {
        this.firebaseAuth = auth;
    }

    @Override
    public boolean checkUserIsSignedIn() {
        return firebaseAuth.getCurrentUser() != null;
    }
}
