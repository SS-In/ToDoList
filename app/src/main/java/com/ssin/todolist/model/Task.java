package com.ssin.todolist.model;

import com.ssin.todolist.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindString;

/**
 * Created by SS-In on 2018-07-07.
 */

public class Task implements Taskable{

    @Override
    public boolean isSection() {
        return false;
    }

    private String title;

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

    private String date;
    private String time;

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
}
