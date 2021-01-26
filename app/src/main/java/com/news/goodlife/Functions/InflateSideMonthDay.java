package com.news.goodlife.Functions;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;

import com.news.goodlife.Interfaces.SuccessCallback;
import com.news.goodlife.Models.CalendarLayoutDay;
import com.news.goodlife.R;

public class InflateSideMonthDay {

    ViewGroup parentCont;
    SuccessCallback successCallback;
    CalendarLayoutDay dayData;

    public InflateSideMonthDay(AsyncLayoutInflater inflater, ViewGroup parent, CalendarLayoutDay dayData, SuccessCallback successCallback) {

        this.parentCont = parent;
        this.successCallback = successCallback;
        this.dayData = dayData;
        inflater.inflate(R.layout.wallet_side_month_dayitem, parent, callback);
    }

    final AsyncLayoutInflater.OnInflateFinishedListener callback = new AsyncLayoutInflater.OnInflateFinishedListener() {
        @Override
        public void onInflateFinished(@NonNull View view, int resid, @Nullable ViewGroup parent) {
            view.setTag(dayData.getMONTH_DAY_NUMBER());
            TextView dayNumber = view.findViewById(R.id.sidemonth_daynumber);
            dayNumber.setText(dayData.getMONTH_DAY_NUMBER());

            parentCont.addView(view);
            successCallback.success();
        }
    };
}
