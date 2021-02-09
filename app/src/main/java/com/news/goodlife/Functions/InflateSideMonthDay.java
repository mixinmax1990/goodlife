package com.news.goodlife.Functions;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;

import com.news.goodlife.Interfaces.SuccessCallback;
import com.news.goodlife.Models.CalendarLayoutDay;
import com.news.goodlife.Processing.Models.DayDataModel;
import com.news.goodlife.R;

import java.util.Calendar;
import java.util.Date;

public class InflateSideMonthDay {

    ViewGroup parentCont;
    SuccessCallback successCallback;
    Date dayDate;
    String Day;

    public InflateSideMonthDay(AsyncLayoutInflater inflater, ViewGroup parent, String Day, SuccessCallback successCallback) {

        this.parentCont = parent;
        this.successCallback = successCallback;
        this.Day = Day;
        inflater.inflate(R.layout.wallet_side_month_dayitem, parent, callback);
    }

    final AsyncLayoutInflater.OnInflateFinishedListener callback = new AsyncLayoutInflater.OnInflateFinishedListener() {
        @Override
        public void onInflateFinished(@NonNull View view, int resid, @Nullable ViewGroup parent) {
            view.setTag(Day);
            TextView dayNumber = view.findViewById(R.id.sidemonth_daynumber);
            dayNumber.setText(Day);

            parent.addView(view);
            successCallback.success();
        }
    };
}
