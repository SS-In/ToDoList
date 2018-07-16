package com.ssin.todolist.ui.register.module;

import com.ssin.todolist.module.FirebaseModule;
import com.ssin.todolist.ui.register.view.RegisterActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by SS-In on 2018-07-16.
 */
@Singleton
@Component(modules = {FirebaseModule.class, RegisterModule.class})
public interface RegisterComponent {
    void inject(RegisterActivity activity);
}
