package com.news.goodlife.Data.Local.Controller;

import android.content.Context;

import com.news.goodlife.Data.Local.Controller.Financial.FinancialEventController;
import com.news.goodlife.Data.Local.Controller.Financial.FinancialProfileController;

public class DatabaseController {

    public TestDataController TestData;
    public FinancialEventController FinancialEvent;
    public FinancialProfileController FinancialProfile;

    public DatabaseController(Context context) {

        this.TestData = new TestDataController(context);
        this.FinancialEvent = new FinancialEventController(context);
        this.FinancialProfile = new FinancialProfileController(context);
    }
}
