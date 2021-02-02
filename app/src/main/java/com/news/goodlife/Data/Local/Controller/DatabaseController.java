package com.news.goodlife.Data.Local.Controller;

import android.content.Context;

import com.news.goodlife.Data.Local.Controller.Financial.AccountsController;
import com.news.goodlife.Data.Local.Controller.Financial.BalanceController;
import com.news.goodlife.Data.Local.Controller.Financial.BudgetCategoryController;
import com.news.goodlife.Data.Local.Controller.Financial.BudgetController;
import com.news.goodlife.Data.Local.Controller.Financial.CounterPartyController;
import com.news.goodlife.Data.Local.Controller.Financial.FixedCostsController;
import com.news.goodlife.Data.Local.Controller.Financial.FixedIncomeController;
import com.news.goodlife.Data.Local.Controller.Financial.TransactionController;
import com.news.goodlife.Data.Local.Controller.Financial.WalletEventController;
import com.news.goodlife.Data.Local.Controller.Financial.FinancialProfileController;
import com.news.goodlife.Data.Local.Controller.KlarnaConsent.KlarnaConsentDBController;
import com.news.goodlife.Data.Local.Models.Financial.FixedCostModel;
import com.news.goodlife.Data.Local.Models.Financial.FixedIncomeModel;
import com.news.goodlife.Data.Remote.Klarna.Controller.KlarnaConsentController;

public class DatabaseController {

    public TestDataController TestData;
    public WalletEventController WalletEvent;
    public FinancialProfileController FinancialProfile;
    public BudgetCategoryController BudgetCategoryController;
    public BudgetController BudgetController;
    public KlarnaConsentDBController KlarnaConsentDBController;
    public AccountsController AccountsController;
    public TransactionController TransactionController;
    public CounterPartyController CounterPartyController;
    public BalanceController BalanceController;
    public FixedCostsController FixedCostsController;
    public FixedIncomeController FixedIncomeController;

    public DatabaseController(Context context) {

        this.TestData = new TestDataController(context);
        this.WalletEvent = new WalletEventController(context);
        this.FinancialProfile = new FinancialProfileController(context);
        this.BudgetCategoryController = new BudgetCategoryController(context);
        this.BudgetController = new BudgetController(context);
        this.KlarnaConsentDBController = new KlarnaConsentDBController(context);
        this.AccountsController = new AccountsController(context);
        this.TransactionController = new TransactionController(context);
        this.CounterPartyController = new CounterPartyController(context);
        this.BalanceController = new BalanceController(context);
        this.FixedCostsController = new FixedCostsController(context);
        this.FixedIncomeController = new FixedIncomeController(context);

    }
}
