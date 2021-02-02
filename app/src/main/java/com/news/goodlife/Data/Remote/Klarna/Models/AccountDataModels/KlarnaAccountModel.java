package com.news.goodlife.Data.Remote.Klarna.Models.AccountDataModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KlarnaAccountModel {

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("alias")
    @Expose
    String alias;

    @SerializedName("account_number")
    @Expose
    String account_number;

    @SerializedName("iban")
    @Expose
    String iban;

    @SerializedName("holder_name")
    @Expose
    String holder_name;

    @SerializedName("bic")
    @Expose
    String bic;

    @SerializedName("bank_address")
    @Expose
    BankAddress bank_address;

    @SerializedName("transfer_type")
    @Expose
    String transfer_type;

    @SerializedName("account_type")
    @Expose
    String account_type;

    public String getId() {
        return id;
    }

    public String getAlias() {
        return alias;
    }

    public String getAccount_number() {
        return account_number;
    }

    public String getIban() {
        return iban;
    }

    public String getHolder_name() {
        return holder_name;
    }

    public String getBic() {
        return bic;
    }

    public BankAddress getBank_address() {
        return bank_address;
    }

    public String getTransfer_type() {
        return transfer_type;
    }

    public String getAccount_type() {
        return account_type;
    }

    public class BankAddress{
        @SerializedName("country")
        @Expose
        String country;

        public String getCountry() {
            return country;
        }
    }
}
