package com.news.goodlife.Data.Remote.Klarna.Models.SessionModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GETSessionData {
    @SerializedName("data")
    @Expose
    GetSessionDataModel data;

    public GetSessionDataModel getData() {
        return data;
    }

    public class GetSessionDataModel {
        @SerializedName("session_id")
        @Expose
        String session_id;

        @SerializedName("session_id_short")
        @Expose
        String session_id_short;

        @SerializedName("state")
        @Expose
        String state;

        @SerializedName("current_flow")
        @Expose
        CurrentFlowModel current_flow;

        @SerializedName("previous_flows")
        @Expose
        List<CurrentFlowModel> previous_flows;

        @SerializedName("bank")
        @Expose
        BankModel bank;

        public String getSession_id() {
            return session_id;
        }

        public String getSession_id_short() {
            return session_id_short;
        }

        public String getState() {
            return state;
        }

        public CurrentFlowModel getCurrent_flow() {
            return current_flow;
        }

        public List<CurrentFlowModel> getPrevious_flows() {
            return previous_flows;
        }

        public BankModel getBank() {
            return bank;
        }
    }

    public class CurrentFlowModel {
        @SerializedName("flow_id")
        @Expose
        String flow_id;

        @SerializedName("url")
        @Expose
        String url;

        @SerializedName("type")
        @Expose
        String type;

        public String getFlow_id() {
            return flow_id;
        }

        public String getUrl() {
            return url;
        }

        public String getType() {
            return type;
        }
    }


    private class BankModel {
    }
}
