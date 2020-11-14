package com.news.goodlife.Interfaces;

import com.news.goodlife.Data.Local.Models.Financial.WalletEventModel;

public interface WalletDatabaseEvents {
    void saveNewWalletEvent(WalletEventModel data, int pos);
}
