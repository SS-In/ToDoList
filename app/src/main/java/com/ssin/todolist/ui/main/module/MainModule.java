package com.ssin.todolist.ui.main.module;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.ssin.todolist.ui.main.interactor.MainInteractor;
import com.ssin.todolist.ui.main.interactor.MainInteractorImpl;
import com.ssin.todolist.ui.main.presenter.MainPresenter;
import com.ssin.todolist.ui.main.presenter.MainPresenterImpl;
import com.ssin.todolist.ui.main.view.MainView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by SS-In on 2018-07-15.
 */

@Module
public class MainModule {
    private MainView view;

    public MainModule(MainView view) {
        this.view = view;
    }

    @Singleton
    @Provides
    public MainView getView() {
        return view;
    }

    @Singleton
    @Provides
    public MainPresenter provideMainPresenter(MainView mainView, MainInteractor mainInteractor) {
        return new MainPresenterImpl(mainView, mainInteractor);
    }

    @Singleton
    @Provides
    public MainInteractor provideMainInteractor(DatabaseReference reference, FirebaseAuth auth) {
        return new MainInteractorImpl(reference, auth);
    }
}
