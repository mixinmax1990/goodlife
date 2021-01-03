package com.news.goodlife.Data.Remote.Klarna.Interfaces;

import com.news.goodlife.Data.Remote.Klarna.Models.SessionModels.RequestBody.SessionRequestBody;
import com.news.goodlife.Data.Remote.Klarna.Models.SessionModels.PUTStartSessionDataModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.PUT;

public interface XS2Api {

    @PUT("/xs2a/v1/sessions")
    Call<PUTStartSessionDataModel> getSession(@Body SessionRequestBody sessionRequestBody, @Header("Authorization") String authHeader);
}
