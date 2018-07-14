package com.ssin.todolist.model;

import android.app.PendingIntent;

import com.ssin.todolist.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindString;

/**
 * Created by SS-In on 2018-07-07.
 */

public class Task implements Taskable{
    private int taskId;
    private boolean done;
    private int remindFreq;
    private int repeatFreq;
    private boolean overdue;
    private String title;
    private List<Tag> tags;
    private String date;
    private String time;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public boolean isSection() {
        return false;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getRemindFreq() {
        return remindFreq;
    }

    public void setRemindFreq(int remindFreq) {
        this.remindFreq = remindFreq;
    }

    public int getRepeatFreq() {
        return repeatFreq;
    }

    public void setRepeatFreq(int repeatFreq) {
        this.repeatFreq = repeatFreq;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isOverdue() {
        return overdue;
    }

    public void setOverdue(boolean overdue) {
        this.overdue = overdue;
    }

    public long getDateTimeMilis() {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        try{
            return format.parse(date + " " + time).getTime();
        }catch (ParseException pe){
            pe.printStackTrace();
        }
        return 0;
    }
}
