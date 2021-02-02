package com.news.goodlife.Interfaces;

import com.news.goodlife.Data.Local.Models.Financial.BudgetModel;

public interface SuccessNewBudgetCallback {

    void success(BudgetModel budgetData);
    void error();
}
