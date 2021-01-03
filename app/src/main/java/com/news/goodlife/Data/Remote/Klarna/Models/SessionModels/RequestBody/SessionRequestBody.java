package com.news.goodlife.Data.Remote.Klarna.Models.SessionModels.RequestBody;

import com.news.goodlife.Data.Remote.Klarna.Models.PsuModel;

public class SessionRequestBody {
    String language;
    PsuModel psu;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public PsuModel getPsu() {
        return psu;
    }

    public void setPsu(PsuModel psu) {
        this.psu = psu;
    }


}
