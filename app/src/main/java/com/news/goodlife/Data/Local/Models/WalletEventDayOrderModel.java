package com.news.goodlife.Data.Local.Models;

import com.news.goodlife.Data.Local.Models.Financial.WalletEventModel;

import java.util.List;

public class WalletEventDayOrderModel {

    private int position;
    private List<WalletEventModel> daysData;

    public WalletEventDayOrderModel() {
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<WalletEventModel> getDaysData() {
        return daysData;
    }

    public void setDaysData(List<WalletEventModel> daysData) {
        this.daysData = daysData;
    }
}
