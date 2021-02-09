package com.news.goodlife.Functions;

import android.os.AsyncTask;

import com.news.goodlife.Interfaces.DaysInMonthCallback;
import com.news.goodlife.Processing.Models.DayDataModel;
import com.news.goodlife.Singletons.SingletonClass;

import java.util.ArrayList;
import java.util.List;

public class AsyncGetDaysInMonth extends AsyncTask<String, List<DayDataModel>, List<DayDataModel>> {


    String selectedMonth, selectedYear;

    DaysInMonthCallback daysInMonthCallback;
    public AsyncGetDaysInMonth(String selectedMonth, String selectedYear, DaysInMonthCallback daysInMonthCallback) {
        super();
        this.daysInMonthCallback = daysInMonthCallback;
        this.selectedMonth = selectedMonth;
        this.selectedYear = selectedYear;
        String[] data = new String[2];
        data[0] = selectedMonth;
        data[1] = selectedYear;

        execute(data);
    }

    @Override
    protected List<DayDataModel> doInBackground(String... strings) {
        List<DayDataModel> daysOfCurrentMonth = new ArrayList<>();
        SingletonClass singletonClass = SingletonClass.getInstance();

        for(DayDataModel dayData: singletonClass.getLogicData()){

            if(dayData.getMonthNo().equals(strings[0])){

                if(dayData.getYearNo().equals(strings[1])){
                    daysOfCurrentMonth.add(dayData);
                }

            }

        }

        return daysOfCurrentMonth;
    }

    @Override
    protected void onPostExecute(List<DayDataModel> dayDataModels) {
        super.onPostExecute(dayDataModels);
        daysInMonthCallback.daysInMonthData(dayDataModels);
    }

}
