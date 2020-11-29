package com.news.goodlife.Models;

import java.util.Date;

public class DayCashflowModel {
    Date date;
    float Amount;

    public DayCashflowModel(Date date, float amount) {
        this.date = date;
        Amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public float getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }
}
