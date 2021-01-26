package com.news.goodlife.Data.Remote.Klarna.Models.FlowModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.news.goodlife.Data.Remote.Klarna.Models.AccountDataModels.AccountModel;

public class GETBalanceModel {
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

        @SerializedName("client_token")
        @Expose
        String client_token;

        @SerializedName("state")
        @Expose
        String state;

        public String getState() {
            return state;
        }

        public Result getResult() {
            return result;
        }

        public String getClient_token() {
            return client_token;
        }

        public class Result{
            @SerializedName("account")
            @Expose
            AccountModel account;

            @SerializedName("available")
            @Expose
            Available available;

            @SerializedName("type")
            @Expose
            String type;

            public AccountModel getAccount() {
                return account;
            }

            public String getType() {
                return type;
            }

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
