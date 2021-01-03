package com.news.goodlife.Data.Remote.Klarna.Models.AccountDataModels;

import android.view.SurfaceControl;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionsAllModel {

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

    public class Result {
        //TODO check if a single Transaction crashes the System

        @SerializedName("transactions")
        @Expose
        TransactionModel transactions;

        @SerializedName("from_date")
        @Expose
        String from_date;

        @SerializedName("to_date")
        @Expose
        String to_date;

        public TransactionModel getTransactions() {
            return transactions;
        }

        public String getFrom_date() {
            return from_date;
        }

        public String getTo_date() {
            return to_date;
        }
    }
}
