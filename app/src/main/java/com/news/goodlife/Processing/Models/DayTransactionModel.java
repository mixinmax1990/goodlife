package com.news.goodlife.Processing.Models;

import com.news.goodlife.Data.Local.Models.Financial.TransactionModel;

import java.util.List;

public class DayTransactionModel {
    List<TransactionModel> daysTransactions;
    int daySum;

    public DayTransactionModel(List<TransactionModel> daysTransactions, int daySum) {
        this.daysTransactions = daysTransactions;
        this.daySum = daySum;
    }

    public List<TransactionModel> getDaysTransactions() {
        return daysTransactions;
    }

    public int getDaySum() {
        return daySum;
    }
}
