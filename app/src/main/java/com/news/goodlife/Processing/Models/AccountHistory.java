package com.news.goodlife.Processing.Models;

import com.news.goodlife.Data.Local.Models.Financial.BalanceModel;
import com.news.goodlife.Data.Local.Models.Financial.TransactionModel;

import java.util.List;

public class AccountHistory {

        List<TransactionModel> allTransactions;
        List<BalanceModel> allBalances;

        public AccountHistory( List<TransactionModel> allTransactions, List<BalanceModel> allBalances) {

            this.allBalances = allBalances;
            this.allTransactions = allTransactions;
        }

        public List<TransactionModel> getAllTransactions() {
            return allTransactions;
        }

        public List<BalanceModel> getAllBalances() {
            return allBalances;
        }

}
