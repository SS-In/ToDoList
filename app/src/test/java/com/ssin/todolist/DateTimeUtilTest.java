package com.ssin.todolist;

import com.ssin.todolist.util.DateTimeUtil;

import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;

/**
 * Created by SS-In on 2018-07-19.
 */

public class DateTimeUtilTest {
    private String[] daysOfWeek;
    private String today = "Dzisiaj";
    private String yesterday = "Wczoraj";
    private String tomorrow = "Jutro";
    private int dayOfYear;

    @Before
    public void setUp() {
        daysOfWeek = new String[]{"Poniedziałek", "Wtorek", "Sroda", "Czwartek", "Piątek", "Sobota", "Niedziela"};
        dayOfYear = 200; // 19-07-2018
    }

    @Test
    public void shouldReturnToday() {
        Calendar calendar = Calendar.getInstance();
        //setCalendar(calendar);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String todayDate = format.format(calendar.getTime());
        String result = DateTimeUtil.getDateTimeWithDayName(daysOfWeek, todayDate, "12:00", today, tomorrow, yesterday);
        assertEquals(result, "Dzisiaj 12:00");
    }

    @Test
    public void shouldReturnYesterday() {
        Calendar calendar = Calendar.getInstance();
        //  setCalendar(calendar);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 1);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String yesterdayDate = format.format(calendar.getTime());
        String result = DateTimeUtil.getDateTimeWithDayName(daysOfWeek, yesterdayDate, "12:00", today, tomorrow, yesterday);
        assertEquals(result, "Wczoraj 12:00");
    }

    @Test
    public void shouldReturnTomorrow() {
        Calendar calendar = Calendar.getInstance();
        //  setCalendar(calendar);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String tomorrowDate = format.format(calendar.getTime());
        String result = DateTimeUtil.getDateTimeWithDayName(daysOfWeek, tomorrowDate, "12:00", today, tomorrow, yesterday);
        assertEquals(result, "Jutro 12:00");
    }

    @Test
    public void shouldReturnActualDayNamePast() {
        Calendar calendar = Calendar.getInstance();
        setCalendar(calendar);
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR); // 19-07-2018, Thursday

        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear - 2);
        assertEquals("Wtorek", DateTimeUtil.getDayName(calendar.get(Calendar.DAY_OF_WEEK), daysOfWeek));

        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear - 3);
        assertEquals("Poniedziałek", DateTimeUtil.getDayName(calendar.get(Calendar.DAY_OF_WEEK), daysOfWeek));

        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear - 4);
        assertEquals("Niedziela", DateTimeUtil.getDayName(calendar.get(Calendar.DAY_OF_WEEK), daysOfWeek));

        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear - 5);
        assertEquals("Sobota", DateTimeUtil.getDayName(calendar.get(Calendar.DAY_OF_WEEK), daysOfWeek));

        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear - 6);
        assertEquals("Piątek", DateTimeUtil.getDayName(calendar.get(Calendar.DAY_OF_WEEK), daysOfWeek));

        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear - 7);
        assertEquals("Czwartek", DateTimeUtil.getDayName(calendar.get(Calendar.DAY_OF_WEEK), daysOfWeek));

        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear - 8);
        assertEquals("Sroda", DateTimeUtil.getDayName(calendar.get(Calendar.DAY_OF_WEEK), daysOfWeek));
    }

    @Test
    public void shouldReturnActualDayNameFuture() {
        Calendar calendar = Calendar.getInstance();
        setCalendar(calendar);

        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        for (int i = 2; i < 7; i++) {
            calendar.set(Calendar.DAY_OF_YEAR, dayOfYear + i);


//            assertEquals(daysOfWeek[calendar.get(Calendar.DAY_OF_WEEK)], DateTimeUtil.getDayName(calendar.get(Calendar.DAY_OF_WEEK), daysOfWeek));
        }


    }

    @Test
    public void shouldReturnDayName() {
        System.out.println("shouldReturnDayName");
        Calendar calendar = Calendar.getInstance();
        setCalendar(calendar);

        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear - 2);
        String tuesday = format.format(calendar.getTime());
        System.out.println("Date: " + tuesday);
        assertEquals(DateTimeUtil.getDateTimeWithDayName(daysOfWeek, tuesday, "12:00", today, tomorrow, yesterday), "Wtorek 12:00");

        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear - 3);
        String monday = format.format(calendar.getTime());
        System.out.println("Date: " + monday);
        assertEquals(DateTimeUtil.getDateTimeWithDayName(daysOfWeek, monday, "12:00", today, tomorrow, yesterday), "Poniedziałek 12:00");

        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear - 4);
        String sunday = format.format(calendar.getTime());
        System.out.println("Date: " + sunday);
        assertEquals(DateTimeUtil.getDateTimeWithDayName(daysOfWeek, sunday, "12:00", today, tomorrow, yesterday), "Niedziela 12:00");

        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear - 5);
        String saturday = format.format(calendar.getTime());
        System.out.println("Date: " + saturday);
        assertEquals(DateTimeUtil.getDateTimeWithDayName(daysOfWeek, saturday, "12:00", today, tomorrow, yesterday), "Sobota 12:00");

        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear - 6);
        String friday = format.format(calendar.getTime());
        System.out.println("Date: " + friday);
        assertEquals(DateTimeUtil.getDateTimeWithDayName(daysOfWeek, friday, "12:00", today, tomorrow, yesterday), "Piątek 12:00");

        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear - 7);
        String thursday = format.format(calendar.getTime());
        System.out.println("Date: " + thursday);
        assertEquals(DateTimeUtil.getDateTimeWithDayName(daysOfWeek, thursday, "12:00", today, tomorrow, yesterday), "Czwartek 12:00");

        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear - 8);
        String wednesday = format.format(calendar.getTime());
        System.out.println("Date: " + wednesday);
        assertEquals(DateTimeUtil.getDateTimeWithDayName(daysOfWeek, wednesday, "12:00", today, tomorrow, yesterday), "Sroda 12:00");
    }

    @Test
    public void shouldReturnDate() {
        Calendar calendar = Calendar.getInstance();
        System.out.println("Time milis: " + System.currentTimeMillis());
        //calendar.setTimeInMillis();
    }

    private static void setCalendar(Calendar calendar) {
        calendar.set(Calendar.YEAR, 2018);
        calendar.set(Calendar.DAY_OF_MONTH, 19);
        calendar.set(Calendar.MONTH, Calendar.JULY);
        calendar.set(Calendar.DAY_OF_YEAR, 200);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
        calendar.setTimeInMillis(1532009351209L);
        System.out.println("DAte: " + calendar.getTime().toString());
    }
}
