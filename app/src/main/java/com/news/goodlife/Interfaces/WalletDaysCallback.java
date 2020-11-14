package com.news.goodlife.Interfaces;

import com.news.goodlife.Data.Local.Models.Financial.WalletEventModel;

public interface WalletDaysCallback {
    void storedNewEntry(WalletEventModel entry, int pos);
}
