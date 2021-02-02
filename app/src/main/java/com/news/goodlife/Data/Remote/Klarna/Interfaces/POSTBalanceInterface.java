package com.news.goodlife.Data.Remote.Klarna.Interfaces;

import com.news.goodlife.Data.Remote.Klarna.Models.AccountDataModels.KlarnaBalanceModel;
import com.news.goodlife.Data.Remote.Klarna.Models.AccountDataModels.KlarnaTransactionsAllModel;
import com.news.goodlife.Data.Remote.Klarna.Models.FlowModels.RequestBody.POSTBalanceRequestBody;
import com.news.goodlife.Data.Remote.Klarna.Models.FlowModels.RequestBody.POSTTransactionsRequestBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface POSTBalanceInterface {

    @POST("balances/get")
    Call<KlarnaBalanceModel> getBalance(@Body POSTBalanceRequestBody requestBody, @Header("Authorization") String authHeader);
}
