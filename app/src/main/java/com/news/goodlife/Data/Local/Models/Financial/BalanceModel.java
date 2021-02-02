package com.news.goodlife.Data.Local.Models.Financial;

public class BalanceModel {
    String id, account_id, amount, currency, timestamp;

    public BalanceModel() {
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String ammount) {
        this.amount = ammount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }
}
