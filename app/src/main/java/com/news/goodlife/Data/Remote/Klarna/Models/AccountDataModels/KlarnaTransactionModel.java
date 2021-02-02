package com.news.goodlife.Data.Remote.Klarna.Models.AccountDataModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KlarnaTransactionModel {

    @SerializedName("transaction_id")
    @Expose
    String transaction_id;

    @SerializedName("reference")
    @Expose
    String reference;

    @SerializedName("counter_party")
    @Expose
    CounterPartyModel counter_party;

    @SerializedName("date")
    @Expose
    String date;

    @SerializedName("value_date")
    @Expose
    String value_date;

    @SerializedName("booking_date")
    @Expose
    String booking_date;

    @SerializedName("state")
    @Expose
    String state;

    @SerializedName("type")
    @Expose
    String type;

    @SerializedName("method")
    @Expose
    String method;

    @SerializedName("amount")
    @Expose
    AmountModel amount;

    public String getTransaction_id() {
        return transaction_id;
    }

    public String getReference() {
        return reference;
    }

    public CounterPartyModel getCounter_party() {
        return counter_party;
    }

    public String getDate() {
        return date;
    }

    public String getValue_date() {
        return value_date;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public String getState() {
        return state;
    }

    public String getType() {
        return type;
    }

    public String getMethod() {
        return method;
    }

    public AmountModel getAmount() {
        return amount;
    }

    public class AmountModel{
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
    public class CounterPartyModel {

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

        @SerializedName("holder_address")
        @Expose
        HolderAddress holder_address;

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

        public HolderAddress getHolder_address() {
            return holder_address;
        }

        public String getTransfer_type() {
            return transfer_type;
        }

        public String getAccount_type() {
            return account_type;
        }


    }
    public class HolderAddress {
        @SerializedName("street_address")
        @Expose
        String street_address;

        @SerializedName("postalcode")
        @Expose
        String postalcode;

        @SerializedName("city")
        @Expose
        String city;

        @SerializedName("region")
        @Expose
        String region;

        @SerializedName("country")
        @Expose
        String country;

        public String getStreet_address() {
            return street_address;
        }

        public String getPostalcode() {
            return postalcode;
        }

        public String getCity() {
            return city;
        }

        public String getRegion() {
            return region;
        }

        public String getCountry() {
            return country;
        }
    }
}
