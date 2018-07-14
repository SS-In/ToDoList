package com.ssin.todolist.ui.main.view;

import com.ssin.todolist.model.Tag;
import com.ssin.todolist.model.Taskable;

import java.util.List;

/**
 * Created by SS-In on 2018-07-11.
 */

public interface MainView {
    void setTaskList(List<Taskable> tasks);
    void setTagsList(List<Tag> tags);
}