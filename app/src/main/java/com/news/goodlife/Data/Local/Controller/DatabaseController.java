package com.news.goodlife.Data.Local.Controller;

import android.content.Context;

import com.news.goodlife.Data.Local.Controller.Financial.WalletEventController;
import com.news.goodlife.Data.Local.Controller.Financial.FinancialProfileController;

public class DatabaseController {

    public TestDataController TestData;
    public WalletEventController WalletEvent;
    public FinancialProfileController FinancialProfile;

    public DatabaseController(Context context) {

        this.TestData = new TestDataController(context);
        this.WalletEvent = new WalletEventController(context);
        this.FinancialProfile = new FinancialProfileController(context);
    }
}
