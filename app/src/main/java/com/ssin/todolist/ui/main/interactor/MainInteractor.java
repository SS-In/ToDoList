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

    interface OnTaskAddListener {
        void onAllTaskFetched(List<Taskable> tasks);

        void onNewTaskAdded(Task task);

        void onTaskFetched(Task task);
    }

    interface TagsListener {
        void onAllTagsFetched(List<Tag> tags);
    }
}


