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
        MainInteractor.TagsListener, MainInteractor.OnGetUserProfileFinishListener, MainInteractor.OnTaskDeleteFinishListener,
        MainInteractor.OnRemoveDoneTasksListener {
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
    public void onAllTaskFetch(boolean showDone) {
        if (!showDone)
            mainInteractor.fetchAllTasks(this);
        else
            mainInteractor.fetchUndoneTasks(this);
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
        mainView.cancelAllAlarms();
        mainView.navigateToSplash();
    }

    @Override
    public void getUserInfo() {
        mainInteractor.getUserProfile(this);
    }

    @Override
    public void onGetProfileFinished(String email, String displayName) {
        mainView.setUserProfile(email, displayName);
        mainView.setAlarms();
    }

    @Override
    public void deleteTask(Task task) {
        mainInteractor.deleteTask(task, this);
    }

    @Override
    public void onTaskDeleted(String taskTitle) {
        mainView.showToastOnSuccess(taskTitle);
    }

    @Override
    public void onTaskDeletedError(String taskTitle) {
        mainView.showToastOnError(taskTitle);
    }

    @Override
    public void fetchOnlyUndoneTasks() {
        mainInteractor.fetchUndoneTasks(this);
    }

    @Override
    public void onDeleteDoneTasksSuccess() {
        mainView.showToastOnClearDoneTasksSuccess();
    }

    @Override
    public void onDeleteDoneTasksError() {
        mainView.showToastOnClearDoneTasksSuccess();
    }

    @Override
    public void clearDoneTasks() {
        mainInteractor.clearDoneTasks(this);
    }

    @Override
    public void filterTasksByName(String taskName) {
        mainInteractor.filterTasksByName(taskName, this);
    }
}
