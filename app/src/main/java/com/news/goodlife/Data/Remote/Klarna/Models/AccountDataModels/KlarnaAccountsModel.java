package com.news.goodlife.Data.Remote.Klarna.Models.AccountDataModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class KlarnaAccountsModel {

    @SerializedName("data")
    @Expose
    KlarnaAccountDetailsModel.Data data;

    public KlarnaAccountDetailsModel.Data getData() {
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
        List<KlarnaAccountModel> account;

        public List<KlarnaAccountModel> getAccount() {
            return account;
        }
    }

}
