package com.ssin.todolist.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by SS-In on 2018-07-14.
 */

public class DateTimeUtil {
    public static String getDateNow(){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        return format.format(calendar.getTime());
    }

    public static String getTimeNow(){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();
        return format.format(calendar.getTime());
    }

    public static String getDateTomorow() {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
        return format.format(calendar.getTime());
    }
}
