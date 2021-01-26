package com.news.goodlife.Models;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarLayoutDay {

        private String type;
        private int position;
        private Calendar calendar;
        private boolean today = false;
        private Date date;
       // public String dayofMonth;

        private String DAY_OF_WEEK_NUMBER, DAY_OF_WEEK_NAME, MONTH_DAY_NUMBER, YEAR, MONTH_NUMBER, MONTH_NAME, MONTH_NAME_SHORT;

        public CalendarLayoutDay(String type, Calendar calendar) {
            this.type = type;
            setCalendar(calendar);

            //Log.i("Calendar", ""+calendar);
            setDAY_OF_WEEK_NAME(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
            setDAY_OF_WEEK_NUMBER(""+calendar.get(Calendar.DAY_OF_WEEK));
            setMONTH_DAY_NUMBER(""+calendar.get(Calendar.DAY_OF_MONTH));
            setYEAR(""+calendar.get(Calendar.YEAR));
            setMONTH_NAME(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
            setMONTH_NAME_SHORT(calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));
            setMONTH_NUMBER(""+calendar.get(Calendar.MONTH));
            setDate(calendar.getTime());

        }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isToday() {
        return today;
    }

    public void setToday(boolean today) {
        this.today = today;
    }

    public String getType() {
            return type;
        }

        public void setType(String type){
            this.type = type;
        }

        public Calendar getCalendar(){
            return calendar;
        }

        public void setCalendar(Calendar calendar){
            this.calendar = calendar;
        }

    public String getDAY_OF_WEEK_NUMBER() {
        return DAY_OF_WEEK_NUMBER;
    }

    public void setDAY_OF_WEEK_NUMBER(String DAY_OF_WEEK_NUMBER) {
        this.DAY_OF_WEEK_NUMBER = DAY_OF_WEEK_NUMBER;
    }

    public String getDAY_OF_WEEK_NAME() {
        return DAY_OF_WEEK_NAME;
    }

    public void setDAY_OF_WEEK_NAME(String DAY_OF_WEEK_NAME) {
        this.DAY_OF_WEEK_NAME = DAY_OF_WEEK_NAME;
    }

    public String getMONTH_DAY_NUMBER() {
        return MONTH_DAY_NUMBER;
    }

    public void setMONTH_DAY_NUMBER(String MONTH_DAY_NUMBER) {
        this.MONTH_DAY_NUMBER = MONTH_DAY_NUMBER;
    }

    public String getYEAR() {
        return YEAR;
    }

    public void setYEAR(String YEAR) {
        this.YEAR = YEAR;
    }

    public String getMONTH_NUMBER() {
        return MONTH_NUMBER;
    }

    public void setMONTH_NUMBER(String MONTH_NUMBER) {
        this.MONTH_NUMBER = MONTH_NUMBER;
    }

    public String getMONTH_NAME() {
        return MONTH_NAME;
    }

    public void setMONTH_NAME(String MONTH_NAME) {
        this.MONTH_NAME = MONTH_NAME;
    }

    public String getMONTH_NAME_SHORT() {
        return MONTH_NAME_SHORT;
    }

    public void setMONTH_NAME_SHORT(String MONTH_NAME_SHORT) {
        this.MONTH_NAME_SHORT = MONTH_NAME_SHORT;
    }
}
