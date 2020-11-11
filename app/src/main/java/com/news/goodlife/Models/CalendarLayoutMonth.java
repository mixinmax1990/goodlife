package com.news.goodlife.Models;

import android.util.Log;

import java.util.List;

public class CalendarLayoutMonth {

    List<CalendarLayoutDay> days;

    public CalendarLayoutMonth(List<CalendarLayoutDay> days) {

        setDays(days);

       // Log.i("DaysSize", ""+getDays().size());
    }

    public List<CalendarLayoutDay> getDays() {
        return days;
    }

    public void setDays(List<CalendarLayoutDay> days) {
        this.days = days;
    }
}
