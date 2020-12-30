package com.news.goodlife.Data.Remote.Klarna;

import com.news.goodlife.Data.Remote.Klarna.Models.SessionRequestBody;
import com.news.goodlife.Data.Remote.Klarna.Models.SessionRequestDataModel;
import com.news.goodlife.Data.Remote.Klarna.Models.XS2ASessionModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.PUT;

public interface XS2Api {

    @PUT("/xs2a/v1/sessions")
    Call<SessionRequestDataModel> getSession(@Body SessionRequestBody sessionRequestBody, @Header("Authorization") String authHeader);
}
