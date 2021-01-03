package com.news.goodlife.Data.Remote.Klarna.Controller;

import android.util.Log;

import com.news.goodlife.Data.Remote.Klarna.Interfaces.DELETEKlarnaFlow;
import com.news.goodlife.Data.Remote.Klarna.Interfaces.KlarnaApiBalanceFlow;
import com.news.goodlife.Data.Remote.Klarna.Interfaces.Callbacks.KlarnaResponseCallback;
import com.news.goodlife.Data.Remote.Klarna.Models.FlowModels.PUTFlowDataModel;
import com.news.goodlife.Data.Remote.Klarna.Models.SessionModels.PUTStartSessionDataModel;
import com.news.goodlife.Singletons.SingletonClass;

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

                Log.i("StartedFlowID", ""+flowData.getData().getFlow_id());
                callback.success();
            }

            @Override
            public void onFailure(Call<PUTFlowDataModel> call, Throwable t) {

                callback.error();
            }
        });

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


}
