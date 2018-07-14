package com.ssin.todolist.model;

import com.ssin.todolist.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindString;
import butterknife.ButterKnife;

/**
 * Created by SS-In on 2018-07-07.
 */

public class TaskHeader implements Taskable{
    private String today;
    private String tomorrow;
    private String date;
    private String time;

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

    @Override
    public boolean isSection() {
        return true;
    }

    public TaskHeader(String todayRsc, String tomorrowRsc) {
        this.today = todayRsc;
        this.tomorrow = tomorrowRsc;
    }

    @Override
    public String toString() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        try{
            if((format.parse(date).getDay()) - calendar.getTime().getDay() == 0){
                return today;
            }   if(format.parse(date).getDay() - calendar.getTime().getDay() == 1){
                return tomorrow;
            }
        }catch (ParseException pe){
            pe.printStackTrace();
        }

        return date;
    }


}
