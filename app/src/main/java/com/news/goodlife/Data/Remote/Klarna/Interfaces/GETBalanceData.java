package com.news.goodlife.Data.Remote.Klarna.Interfaces;

import com.news.goodlife.Data.Remote.Klarna.Models.FlowModels.GETBalanceModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface GETBalanceData {

    @GET("flows/{flow_id}")
    Call<GETBalanceModel> getBalance(@Path("flow_id") String flow_id, @Header("Authorization") String authHeader);
}
