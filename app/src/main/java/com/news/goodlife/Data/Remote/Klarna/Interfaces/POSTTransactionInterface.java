package com.news.goodlife.Data.Remote.Klarna.Interfaces;

import com.news.goodlife.Data.Remote.Klarna.Models.AccountDataModels.KlarnaTransactionsAllModel;
import com.news.goodlife.Data.Remote.Klarna.Models.FlowModels.RequestBody.POSTTransactionsRequestBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface POSTTransactionInterface {

    @POST("transactions/get")
    Call<KlarnaTransactionsAllModel> getTransactions(@Body POSTTransactionsRequestBody requestBody, @Header("Authorization") String authHeader);
}
