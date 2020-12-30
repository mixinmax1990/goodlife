package com.news.goodlife.Data.Remote.Klarna.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SessionFlowsModel {

    @SerializedName("balances")
    @Expose
    String balances;
    @SerializedName("transfer")
    @Expose
    String transfer;
    @SerializedName("account_details")
    @Expose
    String account_details;
    @SerializedName("accounts")
    @Expose
    String accounts;
    @SerializedName("transactions")
    @Expose
    String transactions;

    public String getBalances() {
        return balances;
    }

    public void setBalances(String balances) {
        this.balances = balances;
    }

    public String getTransfer() {
        return transfer;
    }

    public void setTransfer(String transfer) {
        this.transfer = transfer;
    }

    public String getAccount_details() {
        return account_details;
    }

    public void setAccount_details(String account_details) {
        this.account_details = account_details;
    }

    public String getAccounts() {
        return accounts;
    }

    public void setAccounts(String accounts) {
        this.accounts = accounts;
    }

    public String getTransactions() {
        return transactions;
    }

    public void setTransactions(String transactions) {
        this.transactions = transactions;
    }
}
