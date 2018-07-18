package com.ssin.todolist.ui.register.interactor;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Patterns;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * Created by SS-In on 2018-07-16.
 */

public class RegisterInteractorImpl implements RegisterInteractor {
    private FirebaseAuth auth;

    public RegisterInteractorImpl(FirebaseAuth auth) {
        this.auth = auth;
    }

    @Override
    public void register(String email, String password, String confirmPassword, final String displayName, final OnFinishedListener onFinishedListener) {
        boolean error = false;

        if (TextUtils.isEmpty(email)) {
            onFinishedListener.onEmailError();
            error = true;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            onFinishedListener.onInvalidEmailError();
            error = true;
        }

        if (TextUtils.isEmpty(displayName)) {
            onFinishedListener.onDisplayNameError();
            error = true;
        }

        if (TextUtils.isEmpty(password)) {
            onFinishedListener.onPasswordError();
            error = true;
        } else if (!TextUtils.equals(confirmPassword, password)) {
            onFinishedListener.onPasswordsNotMatch();
            error = true;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            onFinishedListener.onConfirmPasswordError();
            error = true;
        }

        if (!error) {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setDisplayName(displayName).build();
                        auth.getCurrentUser().updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                    onFinishedListener.onSuccess();
                                else
                                    onFinishedListener.onServerError(task.getException().getLocalizedMessage());
                            }
                        });
                    } else {
                        onFinishedListener.onServerError(task.getException().getLocalizedMessage());

                    }
                }
            });

            /*        auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    onFinishedListener.onSuccess();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                    onFinishedListener.onServerError();

                }
            });*/
        }
    }
}
