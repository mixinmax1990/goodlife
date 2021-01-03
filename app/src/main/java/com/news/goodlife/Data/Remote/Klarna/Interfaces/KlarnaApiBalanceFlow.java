package com.news.goodlife.Data.Remote.Klarna.Interfaces;

import com.news.goodlife.Data.Remote.Klarna.Models.FlowModels.PUTFlowDataModel;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.PUT;

public interface KlarnaApiBalanceFlow {

    @PUT("flows/balances")
    Call<PUTFlowDataModel> getFlow(@Header("Authorization") String authHeader);
}
