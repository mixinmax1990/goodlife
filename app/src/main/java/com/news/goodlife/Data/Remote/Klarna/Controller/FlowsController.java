package com.news.goodlife.Data.Remote.Klarna.Controller;

import android.util.Log;

import com.news.goodlife.Data.Remote.Klarna.Interfaces.DELETEKlarnaFlow;
import com.news.goodlife.Data.Remote.Klarna.Interfaces.GETBalanceData;
import com.news.goodlife.Data.Remote.Klarna.Interfaces.KlarnaApiBalanceFlow;
import com.news.goodlife.Data.Remote.Klarna.Interfaces.Callbacks.KlarnaResponseCallback;
import com.news.goodlife.Data.Remote.Klarna.Interfaces.POSTTransactionInterface;
import com.news.goodlife.Data.Remote.Klarna.Models.AccountDataModels.TransactionsAllModel;
import com.news.goodlife.Data.Remote.Klarna.Models.Consent.POSTgetConsentDataModel;
import com.news.goodlife.Data.Remote.Klarna.Models.FlowModels.GETBalanceModel;
import com.news.goodlife.Data.Remote.Klarna.Models.FlowModels.KeysModel;
import com.news.goodlife.Data.Remote.Klarna.Models.FlowModels.PUTFlowDataModel;
import com.news.goodlife.Data.Remote.Klarna.Models.FlowModels.RequestBody.POSTTransactionsRequestBody;
import com.news.goodlife.Data.Remote.Klarna.Models.PsuModel;
import com.news.goodlife.Data.Remote.Klarna.Models.SessionModels.PUTStartSessionDataModel;
import com.news.goodlife.Singletons.SingletonClass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FlowsController {

    String token;
    SingletonClass singletonClass = SingletonClass.getInstance();
    PUTFlowDataModel flowData = null;
    GETBalanceModel balanceData = null;


    String activeFlow = null;

    public FlowsController(String token) {
        this.token = token;

    }

    //START BALANCE FLOW
    public void startBalanceFlow(KlarnaResponseCallback callback){

        Retrofit client = getClient("https://api.playground.openbanking.klarna.com/xs2a/v1/sessions/"+singletonClass.getKlarna().getSessionController().getSessionData().getData().getSession_id()+"/");

        KlarnaApiBalanceFlow klarnaApiBalanceFlow = client.create(KlarnaApiBalanceFlow.class);
        Call<PUTFlowDataModel> call = klarnaApiBalanceFlow.getFlow("Bearer "+token);


        //Run Async task to make API call
        call.enqueue(new Callback<PUTFlowDataModel>() {
            @Override
            public void onResponse(Call<PUTFlowDataModel> call, Response<PUTFlowDataModel> response) {
                if (!response.isSuccessful()) {
                    callback.error();
                    return;
                }

                flowData = response.body();

                activeFlow = flowData.getData().getFlow_id();
                //Send The client of the FLow to start the Authentication App

                Log.i("StartedFlowSelf", ""+flowData.getData().getSelf());

                callback.success();
            }

            @Override
            public void onFailure(Call<PUTFlowDataModel> call, Throwable t) {

                callback.error();
            }
        });

    }

    public void getBalanceFlow(String flow_id, KlarnaResponseCallback callback){

        Retrofit client = getClient("https://api.playground.openbanking.klarna.com/xs2a/v1/sessions/"+singletonClass.getKlarna().getSessionController().getSessionData().getData().getSession_id()+"/");

        GETBalanceData getBalanceData = client.create(GETBalanceData.class);
        Call<GETBalanceModel> call = getBalanceData.getBalance(flow_id,"Bearer "+token);


        //Run Async task to make API call
        call.enqueue(new Callback<GETBalanceModel>() {
            @Override
            public void onResponse(Call<GETBalanceModel> call, Response<GETBalanceModel> response) {
                if (!response.isSuccessful()) {
                    callback.error();
                    return;
                }

                balanceData = response.body();

                activeFlow = flowData.getData().getFlow_id();
                //TODO check if the flow has to be finished

                callback.success();
            }

            @Override
            public void onFailure(Call<GETBalanceModel> call, Throwable t) {

                callback.error();
            }
        });

    }

    TransactionsAllModel latestTransactions = null;
    public void getTransactions(String account_klarna_id, KlarnaResponseCallback callback){

        POSTgetConsentDataModel consent = singletonClass.getDatabaseController().KlarnaConsentDBController.getConsent();

        Retrofit client = getClient("https://api.playground.openbanking.klarna.com/xs2a/v1/consents/"+consent.getData().getConsent_id()+"/");

        POSTTransactionInterface postTransactionInterface = client.create(POSTTransactionInterface.class);
        Call<TransactionsAllModel> call = postTransactionInterface.getTransactions(getTranasctionBody(account_klarna_id, consent), "Bearer "+token);


        call.enqueue(new Callback<TransactionsAllModel>() {
            @Override
            public void onResponse(Call<TransactionsAllModel> call, Response<TransactionsAllModel> response) {
                if(!response.isSuccessful()){
                    callback.error();
                    try {
                        Log.i("GETTRANSACTION","FAILED"+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                latestTransactions = response.body();
                Log.i("GETTRANSACTION","Works");
                callback.success();
            }

            @Override
            public void onFailure(Call<TransactionsAllModel> call, Throwable t) {
                callback.error();
                Log.i("GETTRANSACTION","FAILED 2");

            }
        });


    }

    private POSTTransactionsRequestBody getTranasctionBody(String AccountID, POSTgetConsentDataModel consent){
        POSTTransactionsRequestBody requestBody = new POSTTransactionsRequestBody();

        PsuModel psu = new PsuModel();
        KeysModel keys = new KeysModel();

        requestBody.setAccount_id(AccountID);
        requestBody.setConsent_token(consent.getData().getConsent_token());


        //GET TRansactions from the last 3 Months
        requestBody.setFrom_date("2020-10-18");
        requestBody.setTo_date("2021-01-18");

        requestBody.setPsu(psu);
        requestBody.setKeys(keys);

        return requestBody;

    }
    //START ANOTHER FLOW

    public void stopAllFlows(KlarnaResponseCallback callback){

    }

    public void stopFlow(String flowID, KlarnaResponseCallback callback){

        Retrofit retrofit = getClient("https://api.playground.openbanking.klarna.com/xs2a/v1/sessions/"+singletonClass.getKlarna().getSessionController().getSessionData().getData().getSession_id()+"/");

        DELETEKlarnaFlow deleteKlarnaFlow = retrofit.create(DELETEKlarnaFlow.class);
        Call<Response<String>> call = deleteKlarnaFlow.deleteFlow(flowID, "Bearer "+token);

        call.enqueue(new Callback<Response<String>>() {
            @Override
            public void onResponse(Call<Response<String>> call, Response<Response<String>> response) {
                if(!response.isSuccessful()){
                    callback.error();
                    Log.i("Flow Error", "message: "+response.body().message());
                    return;
                }

                Log.i("Flow Success", "message: "+response.message());
                callback.success();

            }

            @Override
            public void onFailure(Call<Response<String>> call, Throwable t) {
                callback.error();
                Log.i("Flow Failure", "message: "+t.getMessage());
            }
        });
    }

    private Retrofit getClient(String baseURL){
        Retrofit retrofit;


        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;

    }

    //Setters and Getters


    public PUTFlowDataModel getFlowData() {
        return flowData;
    }

    public GETBalanceModel getBalanceData() {
        return balanceData;
    }

    public TransactionsAllModel getLatestTransactions() {
        return latestTransactions;
    }
}