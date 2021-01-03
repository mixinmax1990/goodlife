package com.news.goodlife.Data.Remote.Klarna.Models.Consent;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class POSTgetConsentDataModel {

    @SerializedName("data")
    @Expose
    private DataModel data;

    public DataModel getData() {
        return data;
    }

    public void setData() {
        this.data = new DataModel();
    }

    public class DataModel{
        @SerializedName("consent_id")
        @Expose
        private String consent_id;

        @SerializedName("consent_token")
        @Expose
        private String consent_token;

        public String getConsent_id() {
            return consent_id;
        }

        public String getConsent_token() {
            return consent_token;
        }

        public void setConsent_id(String consent_id) {
            this.consent_id = consent_id;
        }

        public void setConsent_token(String consent_token) {
            this.consent_token = consent_token;
        }
    }
}
