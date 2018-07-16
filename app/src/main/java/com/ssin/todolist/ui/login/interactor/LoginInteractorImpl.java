package com.ssin.todolist.ui.login.interactor;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by SS-In on 2018-07-16.
 */

public class LoginInteractorImpl implements LoginInteractor {
    private FirebaseAuth auth;

    public LoginInteractorImpl(FirebaseAuth auth) {
        this.auth = auth;
    }

    @Override
    public void login(String email, String password, final OnLoginFinishedListener listener) {
        boolean error = false;

        if (TextUtils.isEmpty(email)) {
            error = true;
            listener.onEmailError();
        }

        if (TextUtils.isEmpty(password)) {
            error = true;
            listener.onPasswordError();
        }

        if (!error) {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        listener.onSuccess();
                    } else {
                        listener.onError(task.getException().getLocalizedMessage());
                    }
                }
            });
        }
    }
}
