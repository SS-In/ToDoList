package com.ssin.todolist.ui.login.module;

import com.ssin.todolist.module.FirebaseModule;
import com.ssin.todolist.ui.login.view.LoginActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by SS-In on 2018-07-16.
 */

@Singleton
@Component(modules = {FirebaseModule.class, LoginModule.class})
public interface LoginComponent {
    void inject(LoginActivity activity);
}
