package com.news.goodlife.Processing.Models;

import java.util.Date;

public class DayDataModel {


    Date dayDate;
    String type = "day";

    String dayNo, monthNo, yearNo, monthShort;

    //The Hypothetical value assumes a stating Balance of 0; in Live it be come absolute against actual Balances
    int hypertheticalBalance;

    public DayDataModel() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDayDate() {
        return dayDate;
    }

    public void setDayDate(Date dayDate) {
        this.dayDate = dayDate;
    }

    public int getHypertheticalBalance() {
        return hypertheticalBalance;
    }

    public void setHypertheticalBalance(int hypertheticalBalance) {
        this.hypertheticalBalance = hypertheticalBalance;
    }

    public String getDayNo() {
        return dayNo;
    }

    public void setDayNo(String dayNo) {
        this.dayNo = dayNo;
    }

    public String getMonthNo() {
        return monthNo;
    }

    public void setMonthNo(String monthNo) {
        this.monthNo = monthNo;
    }

    public String getYearNo() {
        return yearNo;
    }

    public void setYearNo(String yearNo) {
        this.yearNo = yearNo;
    }

    public String getMonthShort() {
        return monthShort;
    }

    public void setMonthShort(String monthShort) {
        this.monthShort = monthShort;
    }
}
