package com.ssin.todolist.ui.splash.module;

import com.ssin.todolist.module.FirebaseModule;
import com.ssin.todolist.ui.splash.view.SplashActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by SS-In on 2018-07-16.
 */

@Component(modules = {FirebaseModule.class, SplashModule.class})
@Singleton
public interface SplashComponent {
    void inject(SplashActivity activity);
}
