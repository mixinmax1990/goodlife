package com.news.goodlife.Data.Remote.Klarna.Models.FlowModels.RequestBody;

import com.news.goodlife.Data.Remote.Klarna.Models.FlowModels.KeysModel;
import com.news.goodlife.Data.Remote.Klarna.Models.PsuModel;

public class POSTTransactionsRequestBody {
    String consent_token, account_id, from_date, to_date;

    PsuModel psu;
    KeysModel keys;

    public String getConsent_token() {
        return consent_token;
    }

    public void setConsent_token(String consent_token) {
        this.consent_token = consent_token;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public PsuModel getPsu() {
        return psu;
    }

    public void setPsu(PsuModel psu) {
        this.psu = psu;
    }

    public KeysModel getKeys() {
        return keys;
    }

    public void setKeys(KeysModel keys) {
        this.keys = keys;
    }
}
