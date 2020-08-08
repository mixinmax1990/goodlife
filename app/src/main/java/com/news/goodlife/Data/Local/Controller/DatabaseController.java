package com.news.goodlife.Data.Local.Controller;

import android.content.Context;

public class DatabaseController {

    public TestDataController TestData;
    public CashflowController Cashflow;

    public DatabaseController(Context context) {

        this.TestData = new TestDataController(context);
        this.Cashflow = new CashflowController(context);
    }
}
