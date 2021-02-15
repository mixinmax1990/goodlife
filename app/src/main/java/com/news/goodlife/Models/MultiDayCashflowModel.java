package com.news.goodlife.Models;

import com.news.goodlife.CustomViews.DayBudgetShiftChart;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class MultiDayCashflowModel {
    List<DayCashflowModel> MonthCashflow;
    String monthName;

    public MultiDayCashflowModel() {
        MonthCashflow = new ArrayList<>();
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public void addDayCashflow(DayCashflowModel day){
        MonthCashflow.add(day);
    }

    public List<DayCashflowModel> getMonthCashflow() {
        return MonthCashflow;
    }
}
