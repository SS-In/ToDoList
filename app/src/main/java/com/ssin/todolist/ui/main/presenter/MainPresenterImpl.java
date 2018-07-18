package com.ssin.todolist.ui.main.presenter;

import com.ssin.todolist.model.Tag;
import com.ssin.todolist.model.Task;
import com.ssin.todolist.model.Taskable;
import com.ssin.todolist.ui.main.interactor.MainInteractor;
import com.ssin.todolist.ui.main.view.MainView;

import java.util.List;

/**
 * Created by SS-In on 2018-07-15.
 */

public class MainPresenterImpl implements MainPresenter, MainInteractor.OnTaskAddListener, MainInteractor.OnLogoutFinishListener,
        MainInteractor.TagsListener, MainInteractor.OnGetUserProfileFinishListener {
    private MainView mainView;
    private MainInteractor mainInteractor;

    public MainPresenterImpl(MainView mainView, MainInteractor mainInteractor) {
        this.mainView = mainView;
        this.mainInteractor = mainInteractor;
    }

    @Override
    public void onNewTaskAdd(Task task) {
        mainInteractor.addNewTask(task);
    }

    @Override
    public void onNewTaskAdded(Task task) {
        mainView.addNewItem(task);
    }

    @Override
    public void onTaskUpdate(Task task) {
        mainInteractor.updateTask(task);
    }

    @Override
    public void onAllTaskFetch() {
        mainInteractor.fetchAllTasks(this);
    }

    @Override
    public void onAllTaskFetched(List<Taskable> tasks) {
        mainView.setTaskList(tasks);
    }

    @Override
    public void onTaskFetched(Task task) {

    }

    @Override
    public void onAllTagsFetched(List<Tag> tags) {
        mainView.setTagsList(tags);
    }

    @Override
    public void onAllTagsFetch() {
        mainInteractor.fetchAllTags(this);
    }

    @Override
    public void onNewTagAdd(String name) {
        mainInteractor.addNewTag(name);
    }

    @Override
    public void onGetTasksByTag(String tag) {
        mainInteractor.getTasksByTag(tag);
    }

    @Override
    public void onGetTasksByDate(String date) {
        mainInteractor.getTasksByDate(date);
    }

    @Override
    public void logout() {
        mainInteractor.logout(this);
    }

    @Override
    public void onSuccess() {
        mainView.navigateToSplash();
    }

    @Override
    public void getUserInfo() {
        mainInteractor.getUserProfile(this);
    }

    @Override
    public void onGetProfileFinished(String email, String displayName) {
        mainView.setUserProfile(email, displayName);
    }
}
