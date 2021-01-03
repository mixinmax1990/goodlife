package com.news.goodlife.Data.Remote.Klarna.Models.FlowModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PUTFlowDataModel {
    @SerializedName("data")
    @Expose
    FlowDataModel data;

    public FlowDataModel getData() {
        return data;
    }

    public class FlowDataModel {

        @SerializedName("flow_id")
        @Expose
        String flow_id;
        @SerializedName("state")
        @Expose
        String state;
        @SerializedName("self")
        @Expose
        String self;
        @SerializedName("client_token")
        @Expose
        String client_token;

        public String getFlow_id() {
            return flow_id;
        }

        public String getState() {
            return state;
        }

        public String getSelf() {
            return self;
        }

        public String getClient_token() {
            return client_token;
        }
    }
}
