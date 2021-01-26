package com.news.goodlife.Data.Local.Controller;

import android.content.Context;

import com.news.goodlife.Data.Local.Controller.Financial.AccountsController;
import com.news.goodlife.Data.Local.Controller.Financial.BudgetCategoryController;
import com.news.goodlife.Data.Local.Controller.Financial.BudgetController;
import com.news.goodlife.Data.Local.Controller.Financial.WalletEventController;
import com.news.goodlife.Data.Local.Controller.Financial.FinancialProfileController;
import com.news.goodlife.Data.Local.Controller.KlarnaConsent.KlarnaConsentDBController;
import com.news.goodlife.Data.Remote.Klarna.Controller.KlarnaConsentController;

public class DatabaseController {

    public TestDataController TestData;
    public WalletEventController WalletEvent;
    public FinancialProfileController FinancialProfile;
    public BudgetCategoryController BudgetCategoryController;
    public BudgetController BudgetController;
    public KlarnaConsentDBController KlarnaConsentDBController;
    public AccountsController AccountsController;

    public DatabaseController(Context context) {

        this.TestData = new TestDataController(context);
        this.WalletEvent = new WalletEventController(context);
        this.FinancialProfile = new FinancialProfileController(context);
        this.BudgetCategoryController = new BudgetCategoryController(context);
        this.BudgetController = new BudgetController(context);
        this.KlarnaConsentDBController = new KlarnaConsentDBController(context);
        this.AccountsController = new AccountsController(context);
    }
}
