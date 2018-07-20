package com.ssin.todolist.ui.main.interactor;

import com.ssin.todolist.model.Tag;
import com.ssin.todolist.model.Task;
import com.ssin.todolist.model.Taskable;

import java.util.List;

/**
 * Created by SS-In on 2018-07-15.
 */

public interface MainInteractor {
    void addNewTask(Task task);
    void updateTask(Task task);
    void fetchAllTasks(OnTaskAddListener onTaskAddListener);
    void fetchAllTags(TagsListener tagsListener);
    void addNewTag(String name);
    void getTasksByTag(String tag);
    void getTasksByDate(String date);

    void deleteTask(Task task, OnTaskDeleteFinishListener listener);

    void getUserProfile(OnGetUserProfileFinishListener listener);

    interface OnTaskAddListener {
        void onAllTaskFetched(List<Taskable> tasks);
        void onNewTaskAdded(Task task);
        void onTaskFetched(Task task);
    }

    interface TagsListener {
        void onAllTagsFetched(List<Tag> tags);
    }

    interface OnLogoutFinishListener {
        void onSuccess();
    }

    interface OnGetUserProfileFinishListener {
        void onGetProfileFinished(String email, String displayName);
    }

    interface OnTaskDeleteFinishListener {
        void onTaskDeleted(String taskTitle);

        void onTaskDeletedError(String taskTitle);
    }

    interface OnRemoveDoneTasksListener {
        void onDeleteDoneTasksSuccess();

        void onDeleteDoneTasksError();
    }
    void logout(OnLogoutFinishListener listener);

    void fetchUndoneTasks(OnTaskAddListener listener);

    void clearDoneTasks(OnRemoveDoneTasksListener listener);

    void filterTasksByName(String taskName, OnTaskAddListener listener);
}


