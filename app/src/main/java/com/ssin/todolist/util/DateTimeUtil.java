package com.ssin.todolist.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;

/**
 * Created by SS-In on 2018-07-14.
 */

public class DateTimeUtil {
    public static String getDateNow() {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        return format.format(calendar.getTime());
    }

    public static String getTimeNow() {
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

    public static String getDate(int year, int month, int day) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return format.format(calendar.getTime());

    }

    public static String getTime(int hour, int minute) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return format.format(calendar.getTime());
    }

    public static String getDateTimeWithDayName(String[] daysOfWeek, String date, String time, String todayStr, String tomorrowStr, String yesterdayStr) {
        StringTokenizer dateTokenizer = new StringTokenizer(date, "-");

        Calendar calendarNow = Calendar.getInstance();
        Calendar calendarDate = Calendar.getInstance();

        int day = Integer.parseInt(dateTokenizer.nextToken());
        int month = Integer.parseInt(dateTokenizer.nextToken());
        int year = Integer.parseInt(dateTokenizer.nextToken());

        System.out.println(String.format("%d-%d-%d", day, month, year));

        calendarDate.set(Calendar.DAY_OF_MONTH, day);
        calendarDate.set(Calendar.MONTH, month - 1);
        calendarDate.set(Calendar.YEAR, year);


        int dayOfYearNow = calendarNow.get(Calendar.DAY_OF_YEAR);
        int dayOfYearDate = calendarDate.get(Calendar.DAY_OF_YEAR);
        if (dayOfYearNow - dayOfYearDate == -1) {
            return tomorrowStr + " " + time;
        }

        if (dayOfYearNow - dayOfYearDate == 0) {
            return todayStr + " " + time;
        } else if (dayOfYearNow - dayOfYearDate == 1) {
            return yesterdayStr + " " + time;
        } else if ((dayOfYearNow - dayOfYearDate >= 2 && dayOfYearNow - dayOfYearDate <= 8)
                || (dayOfYearNow - dayOfYearDate <= -2 && dayOfYearNow - dayOfYearDate >= -8)) {
            int dayOfWeek = calendarDate.get(Calendar.DAY_OF_WEEK);
            return getDayName(dayOfWeek, daysOfWeek) + " " + time;
        }


        return date + " " + time;
    }

    public static String getDayName(int dayOfWeek, String[] daysOfWeek) {
        Calendar calendar = Calendar.getInstance();
        calendar.get(Calendar.DAY_OF_WEEK);

        switch (dayOfWeek) {
            case Calendar.MONDAY:
                return daysOfWeek[0];
            case Calendar.TUESDAY:
                return daysOfWeek[1];
            case Calendar.WEDNESDAY:
                return daysOfWeek[2];
            case Calendar.THURSDAY:
                return daysOfWeek[3];
            case Calendar.FRIDAY:
                return daysOfWeek[4];
            case Calendar.SATURDAY:
                return daysOfWeek[5];
            case Calendar.SUNDAY:
                return daysOfWeek[6];
        }
        return "";
    }
}
