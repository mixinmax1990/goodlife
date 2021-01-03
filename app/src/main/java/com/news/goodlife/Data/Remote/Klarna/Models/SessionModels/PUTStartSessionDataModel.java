package com.news.goodlife.Data.Remote.Klarna.Models.SessionModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PUTStartSessionDataModel {

    @SerializedName("data")
    @Expose
    XS2ASessionModel data;

    public XS2ASessionModel getData() {
        return data;
    }

    public class XS2ASessionModel {
        @SerializedName("session_id")
        @Expose
        String session_id;
        @SerializedName("session_id_short")
        @Expose
        String session_id_short;
        @SerializedName("self")
        @Expose
        String self;
        @SerializedName("consent")
        @Expose
        String consent;

        @SerializedName("flows")
        @Expose
        SessionFlowsModel flows;

        //String flow_balance, flow_transfer, flow_account_details, flow_accounts, flow_transactions;


        public String getSession_id() {
            return session_id;
        }

        public void setSession_id(String session_id) {
            this.session_id = session_id;
        }

        public String getSession_id_short() {
            return session_id_short;
        }

        public void setSession_id_short(String session_id_short) {
            this.session_id_short = session_id_short;
        }

        public String getSelf() {
            return self;
        }

        public void setSelf(String self) {
            this.self = self;
        }

        public String getConsent() {
            return consent;
        }

        public void setConsent(String consent) {
            this.consent = consent;
        }

        public SessionFlowsModel getFlows() {
            return flows;
        }

        public void setFlows(SessionFlowsModel flows) {
            this.flows = flows;
        }
    }

    public class SessionFlowsModel {

        @SerializedName("balances")
        @Expose
        String balances;
        @SerializedName("transfer")
        @Expose
        String transfer;
        @SerializedName("account_details")
        @Expose
        String account_details;
        @SerializedName("accounts")
        @Expose
        String accounts;
        @SerializedName("transactions")
        @Expose
        String transactions;

        public String getBalances() {
            return balances;
        }

        public void setBalances(String balances) {
            this.balances = balances;
        }

        public String getTransfer() {
            return transfer;
        }

        public void setTransfer(String transfer) {
            this.transfer = transfer;
        }

        public String getAccount_details() {
            return account_details;
        }

        public void setAccount_details(String account_details) {
            this.account_details = account_details;
        }

        public String getAccounts() {
            return accounts;
        }

        public void setAccounts(String accounts) {
            this.accounts = accounts;
        }

        public String getTransactions() {
            return transactions;
        }

        public void setTransactions(String transactions) {
            this.transactions = transactions;
        }
    }


}
