package com.ssin.todolist.ui.main.view;

import com.ssin.todolist.model.Tag;
import com.ssin.todolist.model.Task;
import com.ssin.todolist.model.Taskable;

import java.util.List;

/**
 * Created by SS-In on 2018-07-11.
 */

public interface MainView {
    void setTaskList(List<Taskable> tasks);
    void setTagsList(List<Tag> tags);
    void addNewItem(Task task);

    void navigateToSplash();

    void setUserProfile(String email, String displayName);

    void setAlarms();

    void cancelAllAlarms();

    void showToastOnSuccess(String taskTitle);

    void showToastOnError(String taskTitle);

    void showToastOnClearDoneTasksSuccess();


}
