package com.ssin.todolist.ui.main.presenter;

import com.ssin.todolist.model.Task;

/**
 * Created by SS-In on 2018-07-15.
 */

public interface MainPresenter {
    void onNewTaskAdd(Task task);
    void onTaskUpdate(Task task);
    void onAllTaskFetch();
    void onAllTagsFetch();
    void onNewTagAdd(String name);
    void onGetTasksByTag(String tag);
    void onGetTasksByDate(String date);

    void deleteTask(Task task);

    void fetchOnlyUndoneTasks();

    void clearDoneTasks();

    void getUserInfo();

    void logout();
}
