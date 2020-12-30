package com.news.goodlife.Data.Remote.Klarna.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SessionRequestDataModel {

    @SerializedName("data")
    @Expose
    XS2ASessionModel data;

    public XS2ASessionModel getData() {
        return data;
    }

    public void setData(XS2ASessionModel data) {
        this.data = data;
    }
}
