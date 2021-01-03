package com.news.goodlife.Data.Remote.Klarna.Interfaces;

import com.news.goodlife.Data.Remote.Klarna.Models.Consent.POSTgetConsentDataModel;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface PUTKlarnaConsent {

    @POST("consent/get")
    Call<POSTgetConsentDataModel> getConsent(@Header("Authorization") String authHeader);


}
