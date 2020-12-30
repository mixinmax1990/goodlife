package com.news.goodlife.LayoutManagers;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;

public class WalletCalendarLinearLayoutManager extends LinearLayoutManager {

    private boolean isScrollEnabled = true;
    public WalletCalendarLinearLayoutManager(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled && super.canScrollVertically();
    }
}
