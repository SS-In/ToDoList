package com.ssin.todolist.model;

import com.google.firebase.database.Exclude;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SS-In on 2018-07-07.
 */

public class Task implements Taskable{
    private int taskId;
    private boolean done;
    private int remindFreq;
    private int repeatFreq;
    private String title;
    private Map<String, Tag> tags;
    private String date;
    private String time;
    private String parent;

    public Task() {
        tags = new HashMap<>();
    }

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

    public Map<String, Tag> getTags() {
        return tags;
    }

    public void setTags(Map<String, Tag> tags) {
        this.tags = tags;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    @Exclude
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("taskId", taskId);
        map.put("done", done);
        map.put("remindFreq", remindFreq);
        map.put("repeatFreq", repeatFreq);
        map.put("title", title);
        map.put("tags", tags);
        map.put("date", date);
        map.put("time", time);
        return map;
    }
}
