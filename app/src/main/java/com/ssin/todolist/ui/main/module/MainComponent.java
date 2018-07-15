package com.ssin.todolist.ui.main.module;

import com.ssin.todolist.module.FirebaseModule;
import com.ssin.todolist.ui.main.view.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by SS-In on 2018-07-15.
 */

@Singleton
@Component(modules = {FirebaseModule.class, MainModule.class})
public interface MainComponent {
    void inject(MainActivity activity);
}
