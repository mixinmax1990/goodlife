package com.news.goodlife.Models;

public class toCalendarViewTransition {
    String LiquidView, dateDayName, date;

    public toCalendarViewTransition(String liquidView, String dateDayName, String date) {
        LiquidView = liquidView;
        this.dateDayName = dateDayName;
        this.date = date;
    }

    public String getLiquidView() {
        return LiquidView;
    }

    public void setLiquidView(String liquidView) {
        LiquidView = liquidView;
    }

    public String getDateDayName() {
        return dateDayName;
    }

    public void setDateDayName(String dateDayName) {
        this.dateDayName = dateDayName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
