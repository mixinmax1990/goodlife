package com.news.goodlife.Data.Remote.Klarna.Models.AccountDataModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KlarnaBalanceModel {
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

        public class Result{
            @SerializedName("available")
            @Expose
            Available available;

            public Available getAvailable() {
                return available;
            }

            public class Available{
                @SerializedName("amount")
                @Expose
                int amount;

                @SerializedName("currency")
                @Expose
                String currency;

                public int getAmount() {
                    return amount;
                }

                public String getCurrency() {
                    return currency;
                }
            }
        }

    }
}
