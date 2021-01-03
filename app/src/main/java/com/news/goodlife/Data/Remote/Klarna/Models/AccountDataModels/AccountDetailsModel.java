package com.news.goodlife.Data.Remote.Klarna.Models.AccountDataModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountDetailsModel {

    @SerializedName("data")
    @Expose
    Data data;

    public Data getData() {
        return data;
    }

    public class Data{
        @SerializedName("result")
        @Expose
        Result result;

        @SerializedName("consent_token")
        @Expose
        String consent_token;

        public Result getResult() {
            return result;
        }

        public String getConsent_token() {
            return consent_token;
        }
    }

    public class Result{
        @SerializedName("account")
        @Expose
        AccountModel account;

        public AccountModel getAccount() {
            return account;
        }
    }

}
